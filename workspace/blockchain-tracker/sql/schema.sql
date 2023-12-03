CREATE TABLE IF NOT EXISTS block_sync_log (
    `block_sync_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `lower_block` BIGINT NOT NULL,
    `upper_block` BIGINT NOT NULL,
    `sync_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS nft_ticket (
    `token_id` binary(32) NOT NULL PRIMARY KEY,
    `owner` binary(20) NOT NULL
) ENGINE=InnoDB;