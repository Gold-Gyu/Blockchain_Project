package minter

import (
	"context"
	"errors"
	"fmt"
	"os"
	"strconv"
	"time"

	persistence "eticket.org/blockchain-minter/internal/persistence/generated"
	"go.uber.org/zap"
)

type BackgroundMintOpts struct {
	Ctx               context.Context
	WorkerCount       int
	TaskQueueCapacity int
	TaskPollPeriod    time.Duration
}

type Minter struct {
	logger        *zap.Logger
	queries       *persistence.Queries
	taskQueue     chan any
	taskPublisher *taskPublisher
	mintHelper    *MintHelper

	ctx  context.Context
	stop func()
}

func newTaskQueue(capacity int) (chan any, error) {
	if 0 < capacity {
		return make(chan any, capacity), nil
	} else {
		capacity := os.Getenv("ETICKET_MINTER_TASK_QUEUE_CAPACITY")
		if len(capacity) == 0 {
			return nil, errors.New("missing required environment variable \"ETICKET_MINTER_TASK_QUEUE_CAPACITY\"")
		} else {
			capacity, err := strconv.ParseUint(capacity, 10, 32)
			if err != nil {
				return nil, errors.New("invalid task queue capacity")
			}
			return make(chan any, capacity), nil
		}
	}
}

func (m *Minter) startWorker() {
	for {
		select {
		case task := <-m.taskQueue:
			opts, ok := task.([2]any)
			if !ok {
				m.logger.Warn("unknown task is queued. it'll be ignored.")
				continue
			}

			mintTicketOpts, mtOk := opts[0].(*MintTicketOpts)
			schedulePerformanceOpts, spOk := opts[1].(*SchedulePerformanceOpts)
			if !(mtOk && spOk) {
				m.logger.Warn("unknown task is queued. it'll be ignored.")
				continue
			}

			newlyMinted, err := m.mintHelper.MintTicket(m.ctx, mintTicketOpts, schedulePerformanceOpts)
			if err == nil {
				err = m.queries.MarkAsMinted(m.ctx, mintTicketOpts.ReservationId)
			}

			if err == nil {
				m.logger.Info("ticket minted.",
					zap.Uint32("performanceScheduleId", mintTicketOpts.PerformanceScheduleId),
					zap.Uint32("seatId", mintTicketOpts.SeatId),
					zap.Bool("new", newlyMinted))
			} else {
				m.logger.Warn("failed to mint a ticket", zap.Error(err))
			}

		case <-m.ctx.Done():
			return
		}
	}
}

func (m *Minter) Start(opts BackgroundMintOpts) (err error) {
	const errPrefix = "Minter.Start(): "

	if m.taskQueue, err = newTaskQueue(opts.TaskQueueCapacity); err != nil {
		return fmt.Errorf(errPrefix+"%w", err)
	}

	m.ctx, m.stop = context.WithCancel(opts.Ctx)
	if m.taskPublisher, err = newTaskPublisher(m.logger, m.queries, m.taskQueue); err != nil {
		close(m.taskQueue)
		m.taskQueue = nil
		return fmt.Errorf(errPrefix+"%w", err)
	}

	m.taskPublisher.Start(m.ctx, opts.TaskPollPeriod)
	for i := 0; i < opts.WorkerCount; i++ {
		go m.startWorker()
	}

	return
}

func (m *Minter) Stop() {
	m.stop()
	close(m.taskQueue)
	m.mintHelper.Close()
}

func New(logger *zap.Logger, queries *persistence.Queries) (*Minter, error) {
	const errPrefix = "minter.New(): "

	mintHelper, err := newMintHelperWithEnvvar()
	if err != nil {
		return nil, fmt.Errorf(errPrefix+"%w", err)
	}

	return &Minter{
		logger:        logger,
		queries:       queries,
		taskQueue:     nil,
		taskPublisher: nil,
		mintHelper:    mintHelper,
	}, nil
}
