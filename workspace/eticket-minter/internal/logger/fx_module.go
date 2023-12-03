package logger

import (
	"context"

	"go.uber.org/fx"
	"go.uber.org/zap"
)

func FxModule() fx.Option {
	return fx.Module(
		"logger",

		fx.Provide(
			fx.Annotate(
				New,
				fx.OnStop(func(ctx context.Context, logger *zap.Logger) {
					logger.Sync()
				}),
			),
		),
	)
}
