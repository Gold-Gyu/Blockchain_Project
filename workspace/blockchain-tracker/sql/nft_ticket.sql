-- name: CreateNftTicket :exec
INSERT INTO
    nft_ticket (`token_id`, `owner`)
VALUES
    (?, ?);

-- name: UpdateNftTicket :exec
UPDATE
    nft_ticket
SET
    `owner`=?
WHERE
    `token_id`=?;