// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.22.0
// source: nft.sql

package persistence

import (
	"context"
	"database/sql"
	"strings"
	"time"
)

const bulkMarkAsMinted = `-- name: BulkMarkAsMinted :exec

UPDATE ` + "`" + `reservation` + "`" + `
SET ` + "`" + `status` + "`" + ` = 'MINTED'
WHERE
    ` + "`" + `reservation_id` + "`" + ` IN (/*SLICE:reservations*/?)
`

func (q *Queries) BulkMarkAsMinted(ctx context.Context, reservations []int32) error {
	query := bulkMarkAsMinted
	var queryParams []interface{}
	if len(reservations) > 0 {
		for _, v := range reservations {
			queryParams = append(queryParams, v)
		}
		query = strings.Replace(query, "/*SLICE:reservations*/?", strings.Repeat(",?", len(reservations))[1:], 1)
	} else {
		query = strings.Replace(query, "/*SLICE:reservations*/?", "NULL", 1)
	}
	_, err := q.db.ExecContext(ctx, query, queryParams...)
	return err
}

const confirmedReservations = `-- name: ConfirmedReservations :many

WITH
    ` + "`" + `confirmed_reservations` + "`" + ` AS (
        SELECT
            r.` + "`" + `reservation_id` + "`" + `,
            r.` + "`" + `user_id` + "`" + `,
            r.` + "`" + `performance_schedule_id` + "`" + `,
            r.` + "`" + `seat_id` + "`" + `,
            ps.` + "`" + `start_date_time` + "`" + `
        FROM ` + "`" + `reservation` + "`" + ` r
            INNER JOIN ` + "`" + `performance_schedule` + "`" + ` ps ON r.` + "`" + `performance_schedule_id` + "`" + ` = ps.` + "`" + `performance_schedule_id` + "`" + `
        WHERE
            r.` + "`" + `status` + "`" + ` = 'SOLDOUT'
            AND DATE(ps.` + "`" + `start_date_time` + "`" + `) <= NOW() + INTERVAL 1 DAY
    )
SELECT
    r.` + "`" + `reservation_id` + "`" + `,
    r.` + "`" + `performance_schedule_id` + "`" + `,
    r.` + "`" + `start_date_time` + "`" + ` as ` + "`" + `start_time` + "`" + `,
    r.` + "`" + `user_id` + "`" + `,
    u.` + "`" + `wallet_address` + "`" + `,
    r.` + "`" + `seat_id` + "`" + `
FROM
    ` + "`" + `confirmed_reservations` + "`" + ` r
    INNER JOIN ` + "`" + `user` + "`" + ` u ON r.` + "`" + `user_id` + "`" + ` = u.` + "`" + `id` + "`" + `
LIMIT ?
`

type ConfirmedReservationsRow struct {
	ReservationID         int32
	PerformanceScheduleID int32
	StartTime             time.Time
	UserID                int32
	WalletAddress         sql.NullString
	SeatID                int32
}

func (q *Queries) ConfirmedReservations(ctx context.Context, limit int32) ([]ConfirmedReservationsRow, error) {
	rows, err := q.db.QueryContext(ctx, confirmedReservations, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var items []ConfirmedReservationsRow
	for rows.Next() {
		var i ConfirmedReservationsRow
		if err := rows.Scan(
			&i.ReservationID,
			&i.PerformanceScheduleID,
			&i.StartTime,
			&i.UserID,
			&i.WalletAddress,
			&i.SeatID,
		); err != nil {
			return nil, err
		}
		items = append(items, i)
	}
	if err := rows.Close(); err != nil {
		return nil, err
	}
	if err := rows.Err(); err != nil {
		return nil, err
	}
	return items, nil
}

const markAsMinted = `-- name: MarkAsMinted :exec

UPDATE ` + "`" + `reservation` + "`" + `
SET ` + "`" + `status` + "`" + ` = 'MINTED'
WHERE ` + "`" + `reservation_id` + "`" + ` = ?
`

func (q *Queries) MarkAsMinted(ctx context.Context, reservationID int32) error {
	_, err := q.db.ExecContext(ctx, markAsMinted, reservationID)
	return err
}

const performanceScheduleDetails = `-- name: PerformanceScheduleDetails :one

SELECT
    ` + "`" + `schedule` + "`" + `.` + "`" + `performance_schedule_id` + "`" + `,
    perf.performance_id, perf.cast, perf.description, perf.detail_image_path, perf.genre, perf.poster_image_path, perf.running_time, perf.ticketing_open_date_time, perf.title, perf.concert_hall_id, perf.user_id,
    ` + "`" + `schedule` + "`" + `.` + "`" + `start_date_time` + "`" + `
FROM
    ` + "`" + `performance_schedule` + "`" + ` AS ` + "`" + `schedule` + "`" + `
    JOIN ` + "`" + `performance` + "`" + ` AS ` + "`" + `perf` + "`" + ` ON ` + "`" + `schedule` + "`" + `.` + "`" + `performance_id` + "`" + ` = ` + "`" + `perf` + "`" + `.` + "`" + `performance_id` + "`" + `
WHERE
    ` + "`" + `schedule` + "`" + `.` + "`" + `performance_schedule_id` + "`" + ` = ?
`

type PerformanceScheduleDetailsRow struct {
	PerformanceScheduleID int32
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
	StartDateTime         time.Time
}

func (q *Queries) PerformanceScheduleDetails(ctx context.Context, performanceScheduleID int32) (PerformanceScheduleDetailsRow, error) {
	row := q.db.QueryRowContext(ctx, performanceScheduleDetails, performanceScheduleID)
	var i PerformanceScheduleDetailsRow
	err := row.Scan(
		&i.PerformanceScheduleID,
		&i.PerformanceID,
		&i.Cast,
		&i.Description,
		&i.DetailImagePath,
		&i.Genre,
		&i.PosterImagePath,
		&i.RunningTime,
		&i.TicketingOpenDateTime,
		&i.Title,
		&i.ConcertHallID,
		&i.UserID,
		&i.StartDateTime,
	)
	return i, err
}

const performanceSeats = `-- name: PerformanceSeats :many

SELECT
    ` + "`" + `seat` + "`" + `.` + "`" + `seat_id` + "`" + `,
    ` + "`" + `seat_class` + "`" + `.` + "`" + `class_name` + "`" + ` AS ` + "`" + `seat_class` + "`" + `
FROM
    ` + "`" + `performance_schedule` + "`" + ` AS ` + "`" + `ps` + "`" + `
    JOIN ` + "`" + `performance` + "`" + ` AS ` + "`" + `perf` + "`" + ` ON ` + "`" + `ps` + "`" + `.` + "`" + `performance_id` + "`" + ` = ` + "`" + `perf` + "`" + `.` + "`" + `performance_id` + "`" + `
    JOIN ` + "`" + `concert_hall` + "`" + ` AS ` + "`" + `hall` + "`" + ` ON ` + "`" + `perf` + "`" + `.` + "`" + `concert_hall_id` + "`" + ` = ` + "`" + `hall` + "`" + `.` + "`" + `concert_hall_id` + "`" + `
    JOIN ` + "`" + `section` + "`" + ` AS ` + "`" + `section` + "`" + ` ON ` + "`" + `hall` + "`" + `.` + "`" + `concert_hall_id` + "`" + ` = ` + "`" + `section` + "`" + `.` + "`" + `concert_hall_id` + "`" + `
    JOIN ` + "`" + `section_and_seat_class_relation` + "`" + ` AS ` + "`" + `ssr` + "`" + ` ON ` + "`" + `section` + "`" + `.` + "`" + `section_id` + "`" + ` = ` + "`" + `ssr` + "`" + `.` + "`" + `section_id` + "`" + `
    JOIN ` + "`" + `seat` + "`" + ` AS ` + "`" + `seat` + "`" + ` ON ` + "`" + `ssr` + "`" + `.` + "`" + `section_id` + "`" + ` = ` + "`" + `seat` + "`" + `.` + "`" + `section_id` + "`" + `
    JOIN ` + "`" + `seat_class` + "`" + ` AS ` + "`" + `seat_class` + "`" + ` ON ` + "`" + `ssr` + "`" + `.` + "`" + `seat_class_id` + "`" + ` = ` + "`" + `seat_class` + "`" + `.` + "`" + `seat_class_id` + "`" + `
WHERE
    ` + "`" + `ps` + "`" + `.` + "`" + `performance_schedule_id` + "`" + ` = ?
`

type PerformanceSeatsRow struct {
	SeatID    int32
	SeatClass string
}

func (q *Queries) PerformanceSeats(ctx context.Context, performanceScheduleID int32) ([]PerformanceSeatsRow, error) {
	rows, err := q.db.QueryContext(ctx, performanceSeats, performanceScheduleID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var items []PerformanceSeatsRow
	for rows.Next() {
		var i PerformanceSeatsRow
		if err := rows.Scan(&i.SeatID, &i.SeatClass); err != nil {
			return nil, err
		}
		items = append(items, i)
	}
	if err := rows.Close(); err != nil {
		return nil, err
	}
	if err := rows.Err(); err != nil {
		return nil, err
	}
	return items, nil
}
