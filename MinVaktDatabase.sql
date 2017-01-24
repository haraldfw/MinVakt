DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS shift_competence;
DROP TABLE IF EXISTS user_competence;
DROP TABLE IF EXISTS availability;
DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS competence;
DROP TABLE IF EXISTS image;

-- MySQL Script generated by MySQL Workbench
-- 01/24/17 10:49:38
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema g_scrum05
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema g_scrum05
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `g_scrum05` DEFAULT CHARACTER SET utf8mb4 ;
USE `g_scrum05` ;

-- -----------------------------------------------------
-- Table `g_scrum05`.`competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`competence` (
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`name`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` MEDIUMTEXT NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(128) NOT NULL,
  `salt` VARCHAR(128) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phonenumber` VARCHAR(45) NOT NULL,
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
  REFERENCES `g_scrum05`.`image` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`shift`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`shift` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `absent` BIT NULL DEFAULT 0,
  `standard_hours` DECIMAL(2) NOT NULL,
  `user_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Shift_User1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Shift_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `g_scrum05`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`availability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`availability` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Availability_User1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Availability_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `g_scrum05`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`user_competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`user_competence` (
  `competence_name` VARCHAR(30) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`competence_name`, `user_id`),
  INDEX `fk_Competence_has_User_User1_idx` (`user_id` ASC),
  INDEX `fk_Competence_has_User_Competence_idx` (`competence_name` ASC),
  CONSTRAINT `fk_Competence_has_User_Competence`
  FOREIGN KEY (`competence_name`)
  REFERENCES `g_scrum05`.`competence` (`name`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Competence_has_User_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `g_scrum05`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`shift_competence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`shift_competence` (
  `shift_id` INT NOT NULL,
  `competence_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`shift_id`, `competence_name`),
  INDEX `fk_Shift_has_Competence_Competence1_idx` (`competence_name` ASC),
  INDEX `fk_Shift_has_Competence_Shift1_idx` (`shift_id` ASC),
  CONSTRAINT `fk_Shift_has_Competence_Shift1`
  FOREIGN KEY (`shift_id`)
  REFERENCES `g_scrum05`.`shift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Shift_has_Competence_Competence1`
  FOREIGN KEY (`competence_name`)
  REFERENCES `g_scrum05`.`competence` (`name`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `g_scrum05`.`notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `g_scrum05`.`notification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(300) NOT NULL,
  `action_url` VARCHAR(512) NULL,
  `user_id` INT NULL,
  `competence_name` VARCHAR(30) NULL,
  `closed` BIT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_notification_User1_idx` (`user_id` ASC),
  INDEX `fk_notification_competence1_idx` (`competence_name` ASC),
  CONSTRAINT `fk_notification_User1`
  FOREIGN KEY (`user_id`)
  REFERENCES `g_scrum05`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_notification_competence1`
  FOREIGN KEY (`competence_name`)
  REFERENCES `g_scrum05`.`competence` (`name`)
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
INSERT INTO competence (name) VALUES ('User');
INSERT INTO competence (name) VALUES ('Faglært');
INSERT INTO competence (name) VALUES ('UFaglært');
INSERT INTO competence (name) VALUES ('Sykepleier');
INSERT INTO competence (name) VALUES ('Vernepleier');
INSERT INTO competence (name) VALUES ('Helsefagarbeider');

-- User
INSERT INTO user (username, first_name, last_name, email, phonenumber, employment_percentage, password_hash, salt)
VALUES ('erlentob', 'Erlend', 'Tobiassen', 'erlentob@gmail.com', '95835420', 50,
        'ea880dec00850f06209e5f99553778aa79dfb933797b7edf80990230ea6666acdfc8ff6ce5f7ed0a1799a7f56dfb2665f9dcd5cccf6dfe4f2c1453b58ce59c71',
        'det');

INSERT INTO user (username, first_name, last_name, email, phonenumber, employment_percentage, password_hash, salt)
VALUES ('haraldfw', 'Harald', 'Floor Wilhelmsen', 'haraldfw@gmail.com', '45566637', 25,
        'ea32b66b153c31136f57f56b8852049b390c42cf28a670f30ca94069a2ac1ff499a596f777ab7ea1725c214debe068f9f773581f9cf29ad4c10d80fece5c0ded',
        'det1');

INSERT INTO user (username, first_name, last_name, email, phonenumber, employment_percentage, password_hash, salt)
VALUES ('gardste', 'Gard', 'Steinsvik', 'gardste@gmail.com', '666', 120,
        'ff6c1b38456ebee57b17314aa43d56eb2e2aee1c4bbb1d0b4a7b53b20dab767f772675ae5a9ba6c879ecba2799b8781df114909c019b05dc9abdc2a47a671694',
        'det2');

-- Shift
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-20 10:00:00', '2017-01-21 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-19 10:00:00', '2017-01-20 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-20 10:00:00', '2017-01-20 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-01-30 10:00:00', '2017-01-30 18:00:00', 8, 0);
INSERT INTO shift (user_id, start_time, end_time, standard_hours, absent)
VALUES (1, '2017-02-02 10:00:00', '2017-02-03 18:00:00', 8, 0);
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
VALUES (2, '2017-01-21 20:00:00', '2017-01-22 07:00:00', 14, 0);

-- availability
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-10 00:00:00', '2017-02-10', 1);
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-10 00:00:00', '2017-02-10', 2);
INSERT INTO availability (start_time, end_time, user_id)
VALUES ('2017-01-10 00:00:00', '2017-02-10', 3);

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

-- notifiactions

INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (1, 'Hei på du', NULL, 1, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (2, 'Bare en test, ikke trykk på knappene', 'url', 1, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (3, 'Hei på du', NULL, 2, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (4, 'Bare en test, ikke trykk på knappene', 'url', 2, NULL, 0);
INSERT INTO notification (id, message, action_url, user_id, competence_name, closed)
VALUES (5, 'Denne er lukket, skal ikke sees', NULL, 2, NULL, 1);