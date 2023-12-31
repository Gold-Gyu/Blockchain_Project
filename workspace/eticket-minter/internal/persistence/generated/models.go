// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.22.0

package persistence

import (
	"database/sql"
	"database/sql/driver"
	"fmt"
	"time"
)

type ReservationStatus string

const (
	ReservationStatusCANCEL  ReservationStatus = "CANCEL"
	ReservationStatusMINTED  ReservationStatus = "MINTED"
	ReservationStatusSOLDOUT ReservationStatus = "SOLDOUT"
)

func (e *ReservationStatus) Scan(src interface{}) error {
	switch s := src.(type) {
	case []byte:
		*e = ReservationStatus(s)
	case string:
		*e = ReservationStatus(s)
	default:
		return fmt.Errorf("unsupported scan type for ReservationStatus: %T", src)
	}
	return nil
}

type NullReservationStatus struct {
	ReservationStatus ReservationStatus
	Valid             bool // Valid is true if ReservationStatus is not NULL
}

// Scan implements the Scanner interface.
func (ns *NullReservationStatus) Scan(value interface{}) error {
	if value == nil {
		ns.ReservationStatus, ns.Valid = "", false
		return nil
	}
	ns.Valid = true
	return ns.ReservationStatus.Scan(value)
}

// Value implements the driver Valuer interface.
func (ns NullReservationStatus) Value() (driver.Value, error) {
	if !ns.Valid {
		return nil, nil
	}
	return string(ns.ReservationStatus), nil
}

type BlockSyncLog struct {
	BlockSyncID int32
	LowerBlock  int64
	UpperBlock  int64
	SyncTime    time.Time
}

type ConcertHall struct {
	ConcertHallID      int32
	HallWholeViewImage sql.NullString
	Name               string
	SeatCount          int32
	VenueID            sql.NullInt32
}

type NftTicket struct {
	TokenID []byte
	Owner   []byte
}

type Performance struct {
	PerformanceID         int32
	Cast                  sql.NullString
	Description           sql.NullString
	DetailImagePath       string
	Genre                 string
	PosterImagePath       sql.NullString
	RunningTime           int32
	TicketingOpenDateTime time.Time
	Title                 string
	ConcertHallID         int32
	UserID                int32
}

type PerformanceSchedule struct {
	PerformanceScheduleID int32
	StartDateTime         time.Time
	PerformanceID         int32
}

type Reservation struct {
	ReservationID         int32
	UserID                int32
	PerformanceScheduleID int32
	SeatID                int32
	PaymentAmount         int32
	Status                ReservationStatus
	ReservationTime       time.Time
	CancellationTime      sql.NullTime
}

type Seat struct {
	SeatID    int32
	Number    string
	SeatRow   sql.NullString
	SectionID int32
}

type SeatClass struct {
	SeatClassID   int32
	PerformanceID int32
	ClassName     string
	Price         int32
}

type Section struct {
	SectionID        int32
	Name             string
	SectionSeatCount int32
	ConcertHallID    int32
}

type SectionAndSeatClassRelation struct {
	SectionAndSeatClassRelationID int32
	SeatClassID                   int32
	SectionID                     int32
}

type User struct {
	ID            int32
	Username      string
	Nickname      string
	Password      string
	Email         string
	Role          string
	WalletAddress sql.NullString
}

type Venue struct {
	VenueID   int32
	Address   string
	Latitude  string
	Longitude string
	Name      string
}
