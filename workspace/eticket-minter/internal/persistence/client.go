package persistence

import (
	"database/sql"
	"errors"
	"fmt"
	"os"

	_ "github.com/go-sql-driver/mysql"
)

type MySQLClientConfig struct {
	Url      string
	Username string
	Password string
}

func NewMySQLClientWithEnvvar() (*sql.DB, error) {
	const ERR_PREFIX = "NewMySQLClientWithEnvvar(): "

	url := os.Getenv("ETICKET_DATASOURCE_URL")
	if len(url) == 0 {
		return nil, errors.New(ERR_PREFIX + "missing required environment variable \"ETICKET_DATASOURCE_URL\"")
	}

	username := os.Getenv("ETICKET_DATASOURCE_USERNAME")
	if len(username) == 0 {
		return nil, errors.New(ERR_PREFIX + "missing required environment variable \"ETICKET_DATASOURCE_USERNAME\"")
	}

	return NewMySQLClient(MySQLClientConfig{
		Url:      url,
		Username: username,
		Password: os.Getenv("ETICKET_DATASOURCE_PASSWORD"),
	})
}

func NewMySQLClient(cfg MySQLClientConfig) (*sql.DB, error) {
	db, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s)/eticket?parseTime=true", cfg.Username, cfg.Password, cfg.Url))
	if err != nil {
		return nil, fmt.Errorf("NewMySQLClient(): %w", err)
	}

	db.SetMaxOpenConns(10)
	return db, nil
}
