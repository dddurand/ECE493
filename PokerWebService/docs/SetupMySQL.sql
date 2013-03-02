delimiter $$

DROP DATABASE IF EXISTS `AndroidPoker`$$
CREATE DATABASE `AndroidPoker` /*!40100 DEFAULT CHARACTER SET utf8 */$$

USE `AndroidPoker`$$

DROP TABLE IF EXISTS `game_actions`$$
DROP TABLE IF EXISTS `games`$$
DROP TABLE IF EXISTS `misc_data`$$
DROP TABLE IF EXISTS `user_table`$$

CREATE TABLE `game_actions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountID` int(11) NOT NULL,
  `gameID` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `position` int(11) NOT NULL,
  `hand` varchar(45) DEFAULT NULL,
  `communityCards` varchar(45) DEFAULT NULL,
  `pot` int(11) NOT NULL,
  PRIMARY KEY (`id`,`accountID`,`gameID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8$$

CREATE TABLE `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameUUID` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`gameUUID`),
  UNIQUE KEY `gameUUID_UNIQUE` (`gameUUID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8$$


CREATE TABLE `misc_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` blob NOT NULL,
  PRIMARY KEY (`id`,`accountID`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8$$


CREATE TABLE `user_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `password` mediumtext NOT NULL,
  `auth_token` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `auth_token_UNIQUE` (`auth_token`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8$$


