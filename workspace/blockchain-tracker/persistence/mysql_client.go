package persistence

import (
	"database/sql"
	"fmt"
	"os"

	_ "github.com/go-sql-driver/mysql"
)

func NewMySQLClient() (*sql.DB, error) {
	url := os.Getenv("ETICKET_DATASOURCE_URL")
	if len(url) == 0 {
		return nil, fmt.Errorf("NewMySQLClient(): missing environment variable \"ETICKET_DATASOURCE_URL\"")
	}

	username := os.Getenv("ETICKET_DATASOURCE_USERNAME")
	password := os.Getenv("ETICKET_DATASOURCE_PASSWORD")

	db, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s)/eticket?parseTime=true", username, password, url))
	if err != nil {
		return nil, fmt.Errorf("NewMySQLClient(): %v", err)
	}

	db.SetMaxOpenConns(10)
	db.SetMaxIdleConns(10)

	return db, nil
}
