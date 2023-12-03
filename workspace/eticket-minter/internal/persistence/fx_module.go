package persistence

import (
	"context"
	"database/sql"

	"go.uber.org/fx"

	generated "eticket.org/blockchain-minter/internal/persistence/generated"
)

func FxModule() fx.Option {
	return fx.Module(
		"persistence",

		fx.Provide(
			fx.Annotate(
				NewMySQLClientWithEnvvar,
				fx.OnStop(func(ctx context.Context, conn *sql.DB) error {
					return conn.Close()
				}),
			),

			func(conn *sql.DB) *generated.Queries {
				return generated.New(conn)
			},
		),
	)
}
