package service

import "go.uber.org/fx"

func FxModule() fx.Option {
	return fx.Module(
		"service.fx",

		fx.Provide(
			NewUploadZipService,
			NewUploadJsonService,
			NewUploadService,
		),
	)
}
