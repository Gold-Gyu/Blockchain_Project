package ethutils

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/ethclient"
)

func TxReceipt(ctx context.Context, conn *ethclient.Client, txHash common.Hash, retryDelay time.Duration) (*types.Receipt, error) {
	for {
		receipt, err := conn.TransactionReceipt(ctx, txHash)
		if err == nil {
			return receipt, nil
		}

		if !strings.Contains(err.Error(), "not found") {
			return nil, fmt.Errorf("BlockTransactionReceipt(): %w", err)
		}

		select {
		case <-time.After(retryDelay):
		case <-ctx.Done():
			return nil, fmt.Errorf("BlockTransactionReceipt(): %w", context.Canceled)
		}
	}
}

func TxSucceeded(receipt *types.Receipt) bool {
	return receipt.Status == 1
}
