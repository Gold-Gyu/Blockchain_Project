package main

import (
	"context"
	"runtime"
	"time"

	"eticket.org/blockchain-minter/internal/logger"
	"eticket.org/blockchain-minter/internal/minter"
	"eticket.org/blockchain-minter/internal/persistence"
	"go.uber.org/fx"
)

func main() {
	fx.New(
		logger.FxModule(),
		persistence.FxModule(),

		fx.Provide(
			fx.Annotate(
				minter.New,

				fx.OnStart(func(m *minter.Minter) {
					opts := minter.BackgroundMintOpts{
						Ctx:               context.Background(),
						TaskQueueCapacity: -1,
						WorkerCount:       runtime.NumCPU() * 4,
						TaskPollPeriod:    time.Second * 30,
					}
					m.Start(opts)
				}),

				fx.OnStop(func(m *minter.Minter) {
					m.Stop()
				}),
			),
		),

		fx.Invoke(func(*minter.Minter) {}),
	).Run()

}
