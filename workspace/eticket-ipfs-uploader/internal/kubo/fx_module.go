package kubo

import (
	"net/http"
	"os"

	kuborpc "eticket.org/blockchain-ipfs-uploader/internal/kubo/api/v0"
	"go.uber.org/fx"
)

func FxModule() fx.Option {
	kuboRpcUrl := os.Getenv("ETICKET_KUBO_RPC_URL")
	if len(kuboRpcUrl) == 0 {
		panic("missing required environment variable \"ETICKET_KUBO_RPC_URL\"")
	}

	return fx.Module(
		"kubo",

		fx.Provide(
			func(client *http.Client) *kuborpc.ApiV0 {
				return kuborpc.NewApiV0(client, kuboRpcUrl)
			},
		),
	)
}
