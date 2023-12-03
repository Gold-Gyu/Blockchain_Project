package service

import "go.uber.org/fx"

func NewFxModule() fx.Option {
	return fx.Module("service", fx.Provide(NewSyncNftsService))
}
