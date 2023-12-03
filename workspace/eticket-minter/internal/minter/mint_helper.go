package minter

import (
	"context"
	"errors"
	"fmt"
	"math/big"
	"os"
	"strconv"
	"sync"
	"time"

	contract "eticket.org/blockchain-minter/internal/contract/generated"
	"eticket.org/blockchain-minter/internal/ethutils"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/ethclient"
)

type performanceScheduleState int

const (
	NOT_SCHEDULED  = performanceScheduleState(0)
	TRY_SCHEDULING = performanceScheduleState(1)
	SCHEDULED      = performanceScheduleState(2)
)

type SchedulePerformanceOpts struct {
	PerformanceScheduleId       uint32
	TicketExpirationTime        time.Time
	TicketDefaultContentBaseUrl string
	TicketSpecialContentBaseUrl string
}

type MintTicketOpts struct {
	ReservationId         int32
	Recipient             common.Address
	PerformanceScheduleId uint32
	SeatId                uint32
}

type MintHelperInitOpts struct {
	EthRpcUrl          string
	EthChainId         string
	EthPrivateKey      string
	EthContractAddress string
}

type MintHelper struct {
	ethTxOpts          *bind.TransactOpts
	ethRpcConn         *ethclient.Client
	ethContract        *contract.Contract
	ethContractAddress common.Address

	cachedPerformanceSchedules sync.Map
}

func (m *MintHelper) MinterAddress() common.Address {
	return m.ethTxOpts.From
}

func (m *MintHelper) IsPerformanceScheduled(ctx context.Context, performanceScheduleId uint32) (bool, error) {
	for {
		switch state, _ := m.cachedPerformanceSchedules.LoadOrStore(performanceScheduleId, NOT_SCHEDULED); state {
		case NOT_SCHEDULED:
			if scheduled, err := m.ethContract.IsPerformanceScheduled(&bind.CallOpts{Context: ctx}, performanceScheduleId); err != nil {
				return false, err
			} else {
				if scheduled {
					m.cachedPerformanceSchedules.Store(performanceScheduleId, SCHEDULED)
				}
				return scheduled, err
			}

		case SCHEDULED:
			return true, nil

		case TRY_SCHEDULING:
			select {
			case <-time.After(100 * time.Millisecond):
			case <-ctx.Done():
				return false, fmt.Errorf("mintHelper.IsPerformanceScheduled(): %w", context.Canceled)
			}
		}
	}
}

func (m *MintHelper) doSchedulePerformance(ctx context.Context, opts *SchedulePerformanceOpts) error {
	const errPrefix = "mintHelper.schedulePerformance(): "

	txOpts := &bind.TransactOpts{
		Context:  ctx,
		Signer:   m.ethTxOpts.Signer,
		From:     m.ethTxOpts.From,
		GasPrice: big.NewInt(1e+6),
		GasLimit: 1e+6,
	}

	tx, err := m.ethContract.SchedulePerformance(
		txOpts,
		opts.PerformanceScheduleId,
		big.NewInt(opts.TicketExpirationTime.Unix()),
		opts.TicketDefaultContentBaseUrl,
		opts.TicketSpecialContentBaseUrl)
	if err != nil {
		return fmt.Errorf(errPrefix+"%w", err)
	}

	if receipt, err := ethutils.TxReceipt(ctx, m.ethRpcConn, tx.Hash(), 500*time.Millisecond); err != nil {
		return fmt.Errorf(errPrefix+"%w", context.Canceled)
	} else {
		if ethutils.TxSucceeded(receipt) {
			return nil
		} else {
			return errors.New(errPrefix + "transaction failed")
		}
	}
}

func (m *MintHelper) SchedulePerformance(ctx context.Context, opts *SchedulePerformanceOpts) error {
	const errPrefix = "mintHelper.schedulePerformance(): "

	for {
		switch state, _ := m.cachedPerformanceSchedules.LoadOrStore(opts.PerformanceScheduleId, NOT_SCHEDULED); state {
		case TRY_SCHEDULING:
			select {
			case <-time.After(100 * time.Millisecond):
				continue
			case <-ctx.Done():
				return fmt.Errorf(errPrefix+"%w", context.Canceled)
			}

		case NOT_SCHEDULED:
			if !m.cachedPerformanceSchedules.CompareAndSwap(opts.PerformanceScheduleId, NOT_SCHEDULED, TRY_SCHEDULING) {
				continue
			}

			if scheduled, err := m.ethContract.IsPerformanceScheduled(&bind.CallOpts{Context: ctx}, opts.PerformanceScheduleId); !scheduled {
				if err != nil && errors.Is(err, context.Canceled) {
					m.cachedPerformanceSchedules.Store(opts.PerformanceScheduleId, NOT_SCHEDULED)
					return fmt.Errorf(errPrefix+"%w", err)
				}
				if err := m.doSchedulePerformance(ctx, opts); err != nil {
					m.cachedPerformanceSchedules.Store(opts.PerformanceScheduleId, NOT_SCHEDULED)
					return fmt.Errorf(errPrefix+"%w", err)
				}
			}

			m.cachedPerformanceSchedules.Store(opts.PerformanceScheduleId, SCHEDULED)
		}

		return nil
	}
}

func (m *MintHelper) SetTicketDefaultContentBaseUrl(
	ctx context.Context, performanceScheduleId uint32, ticketDefaultContentBaseUrl string) error {

	txOpts := &bind.TransactOpts{
		Context:  ctx,
		Signer:   m.ethTxOpts.Signer,
		From:     m.ethTxOpts.From,
		GasPrice: big.NewInt(1e+6),
		GasLimit: 1e+6,
	}

	tx, err := m.ethContract.SetTicketDefaultContentBaseUrl(txOpts, performanceScheduleId, ticketDefaultContentBaseUrl)
	if err != nil {
		return err
	}

	receipt, err := ethutils.TxReceipt(ctx, m.ethRpcConn, tx.Hash(), 1*time.Second)
	if err != nil {
		return err
	}
	if !ethutils.TxSucceeded(receipt) {
		return errors.New("ethereum transaction failed")
	}

	return nil
}

func (m *MintHelper) validateOwnership(ctx context.Context, account common.Address, performanceScheduleId, seatId uint32) error {
	owner, err := m.ethContract.TicketOwnerOf(&bind.CallOpts{Pending: true}, performanceScheduleId, seatId)
	if err != nil {
		return fmt.Errorf("minterHelper.validateOwnership(): %w", err)
	}
	if owner.Cmp(account) != 0 {
		return errors.New("minterHelper.validateOwnership(): owner mismatch")
	}
	return nil
}

func (m *MintHelper) doMintTicket(ctx context.Context, recipient common.Address, performanceScheduleId, seatId uint32) error {
	txOpts := &bind.TransactOpts{
		Context:  ctx,
		From:     m.ethTxOpts.From,
		Signer:   m.ethTxOpts.Signer,
		GasPrice: big.NewInt(1e+6),
		GasLimit: 1e+6,
	}

	tx, err := m.ethContract.MintTicket(txOpts, recipient, performanceScheduleId, seatId)
	if err != nil {
		return err
	}

	receipt, err := ethutils.TxReceipt(ctx, m.ethRpcConn, tx.Hash(), 500*time.Millisecond)
	if err == nil && !ethutils.TxSucceeded(receipt) {
		return nil
	} else {
		return err
	}
}

func (m *MintHelper) MintTicket(ctx context.Context, opts *MintTicketOpts, performanceOpts *SchedulePerformanceOpts) (newlyMinted bool, err error) {
	const errPrefix = "mintHelper.MintTicket(): "

	if scheduled, err := m.IsPerformanceScheduled(ctx, opts.PerformanceScheduleId); err != nil {
		return false, fmt.Errorf(errPrefix+"%w", err)
	} else {
		if !scheduled {
			if performanceOpts == nil {
				return false, fmt.Errorf(errPrefix+
					"the performance schedule(id=%d) is not scheduled,"+
					"but it can't be automatically scheduled because *SchedulePerformanceOpts is nil",
					opts.PerformanceScheduleId)
			}
			if err := m.SchedulePerformance(ctx, performanceOpts); err != nil {
				return false, fmt.Errorf(errPrefix+"%w", err)
			}
		}
	}

	minted, err := m.ethContract.IsTicketMinted(&bind.CallOpts{Pending: true}, opts.PerformanceScheduleId, opts.SeatId)
	if err != nil {
		return false, fmt.Errorf(errPrefix+"%w", err)
	}

	if minted {
		if err := m.validateOwnership(ctx, opts.Recipient, opts.PerformanceScheduleId, opts.SeatId); err != nil {
			return false, errors.New(errPrefix + "ticket already minted, but owner is other account")
		}
		return false, nil
	}

	if err := m.doMintTicket(ctx, opts.Recipient, opts.PerformanceScheduleId, opts.SeatId); err != nil {
		return false, fmt.Errorf(errPrefix+"%w", err)
	}

	return true, nil
}

func newMintHelperWithEnvvar() (*MintHelper, error) {
	const errPrefix = "newMintHelperWithEnvvar(): "

	ethRpcUrl := os.Getenv("ETICKET_BLOCKCHAIN_RPC_URL")
	if len(ethRpcUrl) == 0 {
		return nil, errors.New(errPrefix + "missing required environment variable \"ETICKET_BLOCKCHAIN_RPC_URL\"")
	}

	ethChainId := os.Getenv("ETICKET_BLOCKCHAIN_CHAIN_ID")
	if len(ethChainId) == 0 {
		return nil, errors.New(errPrefix + "missing required environment variable \"ETICKET_BLOCKCHAIN_CHAIN_ID\"")
	}

	ethPrivateKey := os.Getenv("ETICKET_BLOCKCHAIN_PRIVATE_KEY")
	if len(ethPrivateKey) == 0 {
		return nil, errors.New(errPrefix + "missing required environment variable \"ETICKET_BLOCKCHAIN_PRIVATE_KEY\"")
	}

	ethContractAddress := os.Getenv("ETICKET_BLOCKCHAIN_CONTRACT_ADDRESS")
	if len(ethContractAddress) == 0 {
		return nil, errors.New(errPrefix + "missing required environment variable \"ETICKET_BLOCKCHAIN_CONTRACT_ADDRESS\"")
	}

	return NewMintHelper(MintHelperInitOpts{
		EthRpcUrl:          ethRpcUrl,
		EthChainId:         ethChainId,
		EthPrivateKey:      ethPrivateKey,
		EthContractAddress: ethContractAddress,
	})
}

func (m *MintHelper) Close() {
	m.ethRpcConn.Close()
}

func NewMintHelper(opts MintHelperInitOpts) (*MintHelper, error) {
	const ERR_PREFIX = "newMintHelper(): "

	ethPrivateKey, err := crypto.ToECDSA(hexutil.MustDecode(opts.EthPrivateKey))
	if err != nil {
		return nil, fmt.Errorf(ERR_PREFIX+": %w", err)
	}

	ethChainId, err := strconv.ParseUint(opts.EthChainId, 10, 64)
	if err != nil {
		return nil, fmt.Errorf(ERR_PREFIX+": %w", err)
	}

	ethTxOpts, err := bind.NewKeyedTransactorWithChainID(ethPrivateKey, big.NewInt(int64(ethChainId)))
	if err != nil {
		return nil, fmt.Errorf(ERR_PREFIX+": %w", err)
	}

	ethRpcConn, err := ethclient.Dial(opts.EthRpcUrl)
	if err != nil {
		return nil, fmt.Errorf(ERR_PREFIX+": %w", err)
	}

	eticketContract, err := contract.NewContract(common.HexToAddress(opts.EthContractAddress), ethRpcConn)
	if err != nil {
		return nil, fmt.Errorf(ERR_PREFIX+": %w", err)
	}

	return &MintHelper{
		ethTxOpts:          ethTxOpts,
		ethRpcConn:         ethRpcConn,
		ethContractAddress: common.HexToAddress(opts.EthContractAddress),
		ethContract:        eticketContract,
	}, nil
}
