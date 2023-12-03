CREATE DATABASE
    IF NOT EXISTS `eticket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `eticket`;

CREATE TABLE
    IF NOT EXISTS `venue` (
        `venue_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `address` VARCHAR(255) NOT NULL,
        `latitude` DECIMAL(38, 2) NOT NULL,
        `longitude` DECIMAL(38, 2) NOT NULL,
        `name` VARCHAR(255) NOT NULL
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `concert_hall` (
        `concert_hall_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `hall_whole_view_image` VARCHAR(255) NULL DEFAULT NULL,
        `name` VARCHAR(255) NOT NULL,
        `seat_count` INT NOT NULL,
        `venue_id` INT DEFAULT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `concert_hall`
ADD
    CONSTRAINT FK__concert_hall__venue FOREIGN KEY (`venue_id`) REFERENCES `venue` (`venue_id`);

CREATE TABLE
    IF NOT EXISTS `user` (
        `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `username` VARCHAR(255) NOT NULL UNIQUE,
        `nickname` VARCHAR(255) NOT NULL,
        `password` VARCHAR(255) NOT NULL,
        `email` VARCHAR(255) NOT NULL,
        `role` VARCHAR(16) NOT NULL,
        `wallet_address` VARBINARY(40) UNIQUE DEFAULT NULL
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `performance` (
        `performance_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `cast` VARCHAR(255) DEFAULT NULL,
        `description` VARCHAR(255) DEFAULT NULL,
        `detail_image_path` VARCHAR(255) NOT NULL,
        `genre` VARCHAR(32) NOT NULL,
        `poster_image_path` VARCHAR(255) NULL DEFAULT NULL,
        `running_time` INT NOT NULL,
        `ticketing_open_date_time` DATETIME(6) NOT NULL,
        `title` VARCHAR(255) NOT NULL,
        `concert_hall_id` INT NOT NULL,
        `user_id` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `performance`
ADD
    CONSTRAINT `FK__performance__concert_hall` FOREIGN KEY (`concert_hall_id`) REFERENCES `concert_hall` (`concert_hall_id`);

ALTER TABLE `performance`
ADD
    CONSTRAINT `FK__performance__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

CREATE TABLE
    IF NOT EXISTS `performance_schedule` (
        `performance_schedule_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `start_date_time` DATETIME NOT NULL,
        `performance_id` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE
    `performance_schedule`
ADD
    CONSTRAINT `FK__performance_schedule__performance` FOREIGN KEY (`performance_id`) REFERENCES `performance` (`performance_id`);

CREATE TABLE
    IF NOT EXISTS `section` (
        `section_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `name` VARCHAR(255) NOT NULL,
        `section_seat_count` INT NOT NULL,
        `concert_hall_id` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `section`
ADD
    CONSTRAINT `FK__section__concert_hall` FOREIGN KEY (`concert_hall_id`) REFERENCES `concert_hall` (`concert_hall_id`);

CREATE TABLE
    IF NOT EXISTS `seat` (
        `seat_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `number` VARCHAR(255) NOT NULL,
        `seat_row` VARCHAR(255) DEFAULT NULL,
        `section_id` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `seat`
ADD
    CONSTRAINT `FK__seat__section` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`);

CREATE TABLE
    IF NOT EXISTS `reservation` (
        `reservation_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `user_id` INT NOT NULL,
        `performance_schedule_id` INT NOT NULL,
        `seat_id` INT NOT NULL,
        `payment_amount` INT NOT NULL,
        `status` ENUM('CANCEL', 'MINTED', 'SOLDOUT') NOT NULL DEFAULT 'SOLDOUT',
        `reservation_time` DATETIME NOT NULL,
        `cancellation_time` DATETIME DEFAULT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `reservation`
ADD
    CONSTRAINT `FK__reservation__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `reservation`
ADD
    CONSTRAINT `FK__reservation__performance_schedule` FOREIGN KEY (`performance_schedule_id`) REFERENCES `performance_schedule` (`performance_schedule_id`);

ALTER TABLE `reservation`
ADD
    CONSTRAINT `FK__reservation__seat` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`);

CREATE TABLE
    IF NOT EXISTS `seat_class` (
        `seat_class_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `performance_id` INT NOT NULL,
        `class_name` VARCHAR(255) NOT NULL,
        `price` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE `seat_class`
ADD
    CONSTRAINT `FK__seat_class__performance` FOREIGN KEY (`performance_id`) REFERENCES `performance` (`performance_id`);

CREATE TABLE
    IF NOT EXISTS `section_and_seat_class_relation` (
        `section_and_seat_class_relation_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `seat_class_id` INT NOT NULL,
        `section_id` INT NOT NULL
    ) ENGINE = InnoDB;

ALTER TABLE
    `section_and_seat_class_relation`
ADD
    CONSTRAINT `FK__section_and_seat_class_relation__section` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`);

ALTER TABLE
    `section_and_seat_class_relation`
ADD
    CONSTRAINT `FK__section_and_seat_class_relation__seat_class` FOREIGN KEY (`seat_class_id`) REFERENCES `seat_class` (`seat_class_id`);

CREATE TABLE
    IF NOT EXISTS `block_sync_log` (
        `block_sync_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `lower_block` BIGINT NOT NULL,
        `upper_block` BIGINT NOT NULL,
        `sync_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE
    IF NOT EXISTS `nft_ticket` (
        `token_id` binary(32) NOT NULL PRIMARY KEY,
        `owner` binary(20) NOT NULL
    );

ALTER TABLE `nft_ticket` ADD INDEX `IDX_nft_ticket_owner` (`owner`);