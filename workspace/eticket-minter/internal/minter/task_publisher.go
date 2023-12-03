package minter

import (
	"context"
	"time"

	persistence "eticket.org/blockchain-minter/internal/persistence/generated"
	"github.com/ethereum/go-ethereum/common"
	"go.uber.org/zap"
)

type taskPublisher struct {
	logger    *zap.Logger
	queries   *persistence.Queries
	taskQueue chan any
}

func (p *taskPublisher) publish(ctx context.Context) {
	reservations, err := p.queries.ConfirmedReservations(ctx, int32(cap(p.taskQueue)))
	if err != nil {
		p.logger.Error("failed to fetch confirmed reservations from datasource.", zap.Error(err))
		return
	}

	numEnqueuedTasks := 0
	for _, reservation := range reservations {
		if len(p.taskQueue) == cap(p.taskQueue) {
			p.logger.Warn("some reservations dropped, because task queue is full. these are re-polled later.")
			break
		}

		year, month, day := reservation.StartTime.Date()
		nextDay := time.Date(year, month, day+1, 0, 0, 0, 0, reservation.StartTime.Location())
		schedulePerformanceOpts := &SchedulePerformanceOpts{
			PerformanceScheduleId:       uint32(reservation.PerformanceScheduleID),
			TicketExpirationTime:        nextDay,
			TicketDefaultContentBaseUrl: "",
			TicketSpecialContentBaseUrl: "",
		}
		mintTicketOpts := &MintTicketOpts{
			ReservationId:         reservation.ReservationID,
			PerformanceScheduleId: uint32(reservation.PerformanceScheduleID),
			Recipient:             common.BytesToAddress([]byte(reservation.WalletAddress.String)),
			SeatId:                uint32(reservation.SeatID),
		}

		p.taskQueue <- [2]any{mintTicketOpts, schedulePerformanceOpts}
		numEnqueuedTasks++
	}

	if 0 < numEnqueuedTasks {
		p.logger.Info("new tasks are enqueued.", zap.Int("numEnqueuedTasks", numEnqueuedTasks))
	}
}

func (p *taskPublisher) Start(ctx context.Context, pollPeriod time.Duration) {
	go func() {
		for {
			select {
			case <-time.After(pollPeriod):
				go p.publish(ctx)
			case <-ctx.Done():
				return
			}
		}
	}()
}

func newTaskPublisher(logger *zap.Logger, queries *persistence.Queries, taskQueue chan any) (*taskPublisher, error) {
	return &taskPublisher{logger: logger, queries: queries, taskQueue: taskQueue}, nil
}
