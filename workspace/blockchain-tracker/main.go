package main

import (
	"context"

	"eticket.org/eticket-tracker/blockchain/tracker"
	"eticket.org/eticket-tracker/config"
	"eticket.org/eticket-tracker/persistence"
	"eticket.org/eticket-tracker/service"
	"go.uber.org/fx"
	"go.uber.org/zap"
)

func main() {
	fx.New(
		service.NewFxModule(),
		persistence.NewFxModule(),

		fx.Provide(
			config.New,
			zap.NewProduction,

			fx.Annotate(
				tracker.New,

				fx.OnStart(func(ctx context.Context, tracker *tracker.ContractTokenTracker) error {
					return tracker.Track(ctx)
				}),

				fx.OnStop(func(ctx context.Context, tracker *tracker.ContractTokenTracker) error {
					return tracker.Stop()
				}),
			),
		),

		fx.Invoke(func(*tracker.ContractTokenTracker) {}),
	).Run()
}
