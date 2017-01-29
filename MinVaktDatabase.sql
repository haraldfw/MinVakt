DROP TABLE IF EXISTS `haraldfw`.notification;
DROP TABLE IF EXISTS `haraldfw`.shift_competence;
DROP TABLE IF EXISTS `haraldfw`.user_competence;
DROP TABLE IF EXISTS `haraldfw`.availability;
DROP TABLE IF EXISTS `haraldfw`.shift;
DROP TABLE IF EXISTS `haraldfw`.user;
DROP TABLE IF EXISTS `haraldfw`.competence;
DROP TABLE IF EXISTS `haraldfw`.image;

-- MySQL Script generated by MySQL Workbench
-- 01/26/17 13:14:38
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema haraldfw
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema haraldfw
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `haraldfw` DEFAULT CHARACTER SET utf8mb4 ;
USE `haraldfw` ;

-- -----------------------------------------------------
-- Table `haraldfw`.`competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`competence` (
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`name`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` MEDIUMTEXT NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(128) NOT NULL,
  `salt` VARCHAR(128) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phonenumber` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `date_of_birth` VARCHAR(45) NOT NULL,
  `employment_percentage` INT NOT NULL,
  `reset_key` VARCHAR(128) NULL,
  `reset_key_expiry` DATETIME NULL,
  `image_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `fk_user_image1_idx` (`image_id` ASC),
  CONSTRAINT `fk_user_image1`
  FOREIGN KEY (`image_id`)
  REFERENCES `haraldfw`.`image` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`shift`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`shift` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `absent` BIT NULL DEFAULT 0,
  `locked` BIT NOT NULL DEFAULT 0,
  `standard_hours` DECIMAL(2) NOT NULL,
  `user_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Shift_User1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Shift_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `haraldfw`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`availability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`availability` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Availability_User1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Availability_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `haraldfw`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`user_competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`user_competence` (
  `competence_name` VARCHAR(30) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`competence_name`, `user_id`),
  INDEX `fk_Competence_has_User_User1_idx` (`user_id` ASC),
  INDEX `fk_Competence_has_User_Competence_idx` (`competence_name` ASC),
  CONSTRAINT `fk_Competence_has_User_Competence`
  FOREIGN KEY (`competence_name`)
  REFERENCES `haraldfw`.`competence` (`name`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Competence_has_User_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `haraldfw`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`shift_competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`shift_competence` (
  `shift_id` INT NOT NULL,
  `competence_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`shift_id`, `competence_name`),
  INDEX `fk_Shift_has_Competence_Competence1_idx` (`competence_name` ASC),
  INDEX `fk_Shift_has_Competence_Shift1_idx` (`shift_id` ASC),
  CONSTRAINT `fk_Shift_has_Competence_Shift1`
  FOREIGN KEY (`shift_id`)
  REFERENCES `haraldfw`.`shift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Shift_has_Competence_Competence1`
  FOREIGN KEY (`competence_name`)
  REFERENCES `haraldfw`.`competence` (`name`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `haraldfw`.`notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `haraldfw`.`notification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(300) NOT NULL,
  `action_url` VARCHAR(256) NULL,
  `redirect_url` VARCHAR(256) NULL,
  `user_id` INT NULL,
  `competence_name` VARCHAR(30) NULL,
  `closed` BIT NULL DEFAULT 0,
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_notification_User1_idx` (`user_id` ASC),
  INDEX `fk_notification_competence1_idx` (`competence_name` ASC),
  CONSTRAINT `fk_notification_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `haraldfw`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_notification_competence1`
  FOREIGN KEY (`competence_name`)
  REFERENCES `haraldfw`.`competence` (`name`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- -----------------------------------------------------
-- DUMMY DATA
-- -----------------------------------------------------

-- Compentences
INSERT INTO competence (name) VALUES ('Admin');
INSERT INTO competence (name) VALUES ('Faglært');
INSERT INTO competence (name) VALUES ('UFaglært');
INSERT INTO competence (name) VALUES ('Sykepleier');
INSERT INTO competence (name) VALUES ('Vernepleier');
INSERT INTO competence (name) VALUES ('Helsefagarbeider');

-- User
INSERT INTO user (username, first_name, last_name, email, phonenumber, address, date_of_birth, employment_percentage, password_hash, salt)
VALUES ('erlentob', 'Erlend', 'Tobiassen', 'erlentob@stud.ntnu.no', '95835420', 'address', 'd_o_b', 50,
        'ea880dec00850f06209e5f99553778aa79dfb933797b7edf80990230ea6666acdfc8ff6ce5f7ed0a1799a7f56dfb2665f9dcd5cccf6dfe4f2c1453b58ce59c71',
        'det');

INSERT INTO user (username, first_name, last_name, email, phonenumber, address, date_of_birth, employment_percentage, password_hash, salt)
VALUES ('haraldfw', 'Harald', 'Floor Wilhelmsen', 'haraldfw@gmail.com', '45566637', 'address', 'i går', 25,
        'ea32b66b153c31136f57f56b8852049b390c42cf28a670f30ca94069a2ac1ff499a596f777ab7ea1725c214debe068f9f773581f9cf29ad4c10d80fece5c0ded',
        'det1');

INSERT INTO user (username, first_name, last_name, email, phonenumber, address, date_of_birth, employment_percentage, password_hash, salt)
VALUES ('gardste', 'Gard', 'Steinsvik', 'gardsteinsvik@gmail.com', '666', 'address', 'd_o_b', 120,
        'ff6c1b38456ebee57b17314aa43d56eb2e2aee1c4bbb1d0b4a7b53b20dab767f772675ae5a9ba6c879ecba2799b8781df114909c019b05dc9abdc2a47a671694',
        'det2');

INSERT INTO user (username, first_name, last_name, email, phonenumber, address, date_of_birth, employment_percentage, password_hash, salt)
VALUES ('olno', 'Ola', 'Nordmann', 'epost@epost.no', '21435643', 'Trondheim', '4/9/77', 10,
        'ff6c1b38456ebee57b17314aa43d56eb2e2aee1c4bbb1d0b4a7b53b20dab767f772675ae5a9ba6c879ecba2799b8781df114909c019b05dc9abdc2a47a671694',
        'det2');


-- Shift
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-21 10:00:00', '2017-01-21 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-20 10:00:00', '2017-01-20 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-30 10:00:00', '2017-01-30 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-02-01 06:00:00', '2017-02-01 18:00:00', 12, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-02-02 10:00:00', '2017-02-02 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-02-03 10:00:00', '2017-02-03 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-25 12:00:00', '2017-01-25 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-26 12:00:00', '2017-01-26 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-27 12:00:00', '2017-01-27 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-28 12:00:00', '2017-01-28 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-29 12:00:00', '2017-01-29 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-30 12:00:00', '2017-01-30 20:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (2, '2017-01-21 20:00:00', '2017-01-22 07:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-25 20:00:00', '2017-01-26 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-26 20:00:00', '2017-01-27 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-27 20:00:00', '2017-01-28 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-28 20:00:00', '2017-01-29 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-29 20:00:00', '2017-01-30 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (3, '2017-01-30 20:00:00', '2017-01-31 10:00:00', 14, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-01-29 20:00:00', '2017-01-30 07:00:00', 11, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-01-30 20:00:00', '2017-01-31 07:00:00', 11, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-01 20:00:00', '2017-02-02 07:00:00', 11, 1);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-02 20:00:00', '2017-02-03 01:00:00', 5, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-04 08:00:00', '2017-02-04 18:00:00', 10, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-05 08:00:00', '2017-02-05 18:00:00', 10, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-06 08:00:00', '2017-02-06 18:00:00', 10, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (4, '2017-02-07 08:00:00', '2017-02-07 18:00:00', 10, 0);

-- availability
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-10 00:00:00', '2017-02-10', 1);
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-10 00:00:00', '2017-02-10', 2);
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-23 00:00:00', '2017-01-23 10:00:00', 3);
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-02-03 06:00:00', '2017-02-03 18:00:00', 4);

-- shift-competence
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 1);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 1);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 2);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 2);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 3);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 4);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 5);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 5);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 6);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 7);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 8);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 9);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 10);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 11);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 12);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 13);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 14);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 15);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 16);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 17);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 18);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 19);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 20);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 21);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 22);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 23);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 24);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Sykepleier', 25);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 26);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 20);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 21);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 22);
INSERT INTO shift_competence (competence_name, shift_id)
VALUES ('Faglært', 23);

-- user-competence
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Sykepleier', 1);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Sykepleier', 2);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Sykepleier', 3);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Faglært', 1);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Faglært', 2);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Faglært', 3);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Admin', 1);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Admin', 2);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Admin', 3);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Faglært', 4);
INSERT INTO user_competence (competence_name, user_id)
VALUES ('Sykepleier', 4);

-- notifiactions
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (1, 'Hei på du', NULL, 3, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (2, 'Dette er en admin notification', 'du_skulle_ikke_trykke!', null, 'Admin', 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (3, 'Hei på du', NULL, 2, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (5, 'Denne er lukket, skal ikke sees', NULL, 2, NULL, 1);