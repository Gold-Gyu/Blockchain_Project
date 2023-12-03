package web

import (
	"context"
	"log"
	"net/http"

	apiV0 "eticket.org/blockchain-ipfs-uploader/web/api/v0"
	"github.com/gin-gonic/gin"
	"go.uber.org/fx"
)

func NewGinEngine() *gin.Engine {
	ginEngine := gin.Default()
	ginEngine.MaxMultipartMemory = 64 << 20 // 64MiB
	return ginEngine
}

func FxModule() fx.Option {
	return fx.Module(
		"web.fx",

		fx.Provide(
			NewGinEngine,

			fx.Annotate(
				func(e *gin.Engine) *http.Server {
					return &http.Server{
						Addr:    ":39880",
						Handler: e,
					}
				},

				fx.OnStart(func(ctx context.Context, serv *http.Server) {
					go func() {
						if err := serv.ListenAndServe(); err != nil {
							log.Fatal(err)
						}
					}()
				}),

				fx.OnStop(func(ctx context.Context, serv *http.Server) error {
					return serv.Close()
				}),
			),
		),

		fx.Invoke(func(serv *http.Server) {}),
		fx.Invoke(apiV0.RegisterUploadJsonApi),
		fx.Invoke(apiV0.RegisterUploadZipApi),
		fx.Invoke(apiV0.RegisterUploadApi),
	)
}
