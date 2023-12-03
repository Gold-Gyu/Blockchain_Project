-- name: GetLatestSyncLog :one
SELECT
    *
FROM
    block_sync_log
ORDER BY
    `block_sync_id` DESC
LIMIT
    1;

-- name: CreateSyncLog :exec
INSERT INTO
    block_sync_log (`lower_block`, `upper_block`)
VALUES
    (?, ?);