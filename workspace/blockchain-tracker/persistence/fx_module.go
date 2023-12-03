package persistence

import (
	"database/sql"

	_persistence "eticket.org/eticket-tracker/persistence/generated"
	"go.uber.org/fx"
)

func NewFxModule() fx.Option {
	return fx.Module(
		"persistence",
		fx.Provide(
			NewMySQLClient,

			func(db *sql.DB) *_persistence.Queries {
				return _persistence.New(db)
			},
		),
	)
}
