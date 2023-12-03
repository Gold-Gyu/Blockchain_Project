package main

import (
	"net/http"

	"eticket.org/blockchain-ipfs-uploader/internal/kubo"
	"eticket.org/blockchain-ipfs-uploader/internal/service"
	"eticket.org/blockchain-ipfs-uploader/web"
	"go.uber.org/fx"
)

func main() {
	fx.New(
		kubo.FxModule(),
		service.FxModule(),
		web.FxModule(),

		fx.Provide(func() *http.Client { return &http.Client{} }),
	).Run()
}
