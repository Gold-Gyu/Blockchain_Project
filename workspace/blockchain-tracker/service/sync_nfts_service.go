package service

import (
	"context"
	"database/sql"
	"strings"

	"eticket.org/eticket-tracker/blockchain"
	persistence "eticket.org/eticket-tracker/persistence/generated"
	"github.com/ethereum/go-ethereum/common"
	"go.uber.org/zap"
)

type SyncNftsService struct {
	db      *sql.DB
	log     *zap.Logger
	queries *persistence.Queries
}

type SyncNftsCommand struct {
	LowerBlock        int64
	UpperBlock        int64
	NftTransferEvents map[[32]byte]*blockchain.NftTransferEvent
}

func (s *SyncNftsService) SyncNfts(ctx context.Context, cmd SyncNftsCommand) error {
	tx, err := s.db.BeginTx(ctx, &sql.TxOptions{Isolation: sql.LevelReadCommitted})
	if err != nil {
		return err
	}

	queries := s.queries.WithTx(tx)
	defer tx.Rollback()

	for _, event := range cmd.NftTransferEvents {
		isNewToken := true
		for i := 0; i < len(event.From); i++ {
			if event.From[i] != 0 {
				isNewToken = false
				break
			}
		}

		if isNewToken {
			if err = queries.CreateNftTicket(ctx, persistence.CreateNftTicketParams{
				TokenID: event.TokenId[:],
				Owner:   event.To[:],
			}); err != nil {
				if !strings.Contains(err.Error(), "Duplicate") {
					return err
				}

				s.log.Warn("token " + common.Bytes2Hex(event.TokenId[:]) + " is duplicated.")
			}
		}

		if !isNewToken {
			if err := queries.UpdateNftTicket(ctx, persistence.UpdateNftTicketParams{
				TokenID: event.TokenId[:],
				Owner:   event.To[:],
			}); err != nil {
				return err
			}
		} else {
		}
	}

	if err = queries.CreateSyncLog(ctx, persistence.CreateSyncLogParams{
		LowerBlock: cmd.LowerBlock,
		UpperBlock: cmd.UpperBlock,
	}); err != nil {
		return err
	}

	return tx.Commit()
}

func NewSyncNftsService(db *sql.DB, queries *persistence.Queries, log *zap.Logger) *SyncNftsService {
	return &SyncNftsService{
		db:      db,
		log:     log,
		queries: queries,
	}
}
