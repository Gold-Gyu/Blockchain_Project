-- name: ConfirmedReservations :many

WITH
    `confirmed_reservations` AS (
        SELECT
            r.`reservation_id`,
            r.`user_id`,
            r.`performance_schedule_id`,
            r.`seat_id`,
            ps.`start_date_time`
        FROM `reservation` r
            INNER JOIN `performance_schedule` ps ON r.`performance_schedule_id` = ps.`performance_schedule_id`
        WHERE
            r.`status` = 'SOLDOUT'
            AND DATE(ps.`start_date_time`) <= NOW() + INTERVAL 1 DAY
    )
SELECT
    r.`reservation_id`,
    r.`performance_schedule_id`,
    r.`start_date_time` as `start_time`,
    r.`user_id`,
    u.`wallet_address`,
    r.`seat_id`
FROM
    `confirmed_reservations` r
    INNER JOIN `user` u ON r.`user_id` = u.`id`
LIMIT ?;

-- name: BulkMarkAsMinted :exec

UPDATE `reservation`
SET `status` = 'MINTED'
WHERE
    `reservation_id` IN (sqlc.slice('reservations'));

-- name: MarkAsMinted :exec

UPDATE `reservation`
SET `status` = 'MINTED'
WHERE `reservation_id` = ?;

-- name: PerformanceSeats :many

SELECT
    `seat`.`seat_id`,
    `seat_class`.`class_name` AS `seat_class`
FROM
    `performance_schedule` AS `ps`
    JOIN `performance` AS `perf` ON `ps`.`performance_id` = `perf`.`performance_id`
    JOIN `concert_hall` AS `hall` ON `perf`.`concert_hall_id` = `hall`.`concert_hall_id`
    JOIN `section` AS `section` ON `hall`.`concert_hall_id` = `section`.`concert_hall_id`
    JOIN `section_and_seat_class_relation` AS `ssr` ON `section`.`section_id` = `ssr`.`section_id`
    JOIN `seat` AS `seat` ON `ssr`.`section_id` = `seat`.`section_id`
    JOIN `seat_class` AS `seat_class` ON `ssr`.`seat_class_id` = `seat_class`.`seat_class_id`
WHERE
    `ps`.`performance_schedule_id` = ?;

-- name: PerformanceScheduleDetails :one

SELECT
    `schedule`.`performance_schedule_id`,
    `perf`.*,
    `schedule`.`start_date_time`
FROM
    `performance_schedule` AS `schedule`
    JOIN `performance` AS `perf` ON `schedule`.`performance_id` = `perf`.`performance_id`
WHERE
    `schedule`.`performance_schedule_id` = ?;