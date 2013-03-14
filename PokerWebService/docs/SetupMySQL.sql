delimiter $$

DROP DATABASE IF EXISTS `AndroidPoker`$$
CREATE DATABASE `AndroidPoker` /*!40100 DEFAULT CHARACTER SET utf8 */$$

USE `AndroidPoker`$$

DROP TABLE IF EXISTS `game_actions`$$
DROP TABLE IF EXISTS `games`$$
DROP TABLE IF EXISTS `misc_data`$$
DROP TABLE IF EXISTS `user_table`$$

delimiter $$

CREATE TABLE `game_actions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountID` int(11) NOT NULL,
  `gameID` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `position` int(11) NOT NULL,
  `hand` varchar(45) DEFAULT NULL,
  `communityCards` varchar(45) DEFAULT NULL,
  `pot` int(11) NOT NULL DEFAULT '0',
  `bet` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`accountID`,`gameID`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `game_optimality` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `optimality` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameUUID` varchar(45) NOT NULL,
  `date_uploaded` date NOT NULL,
  PRIMARY KEY (`id`,`gameUUID`),
  UNIQUE KEY `gameUUID_UNIQUE` (`gameUUID`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `misc_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` blob NOT NULL,
  `date_uploaded` date NOT NULL,
  PRIMARY KEY (`id`,`accountID`)
) ENGINE=InnoDB AUTO_INCREMENT=236 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `user_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `password` mediumtext NOT NULL,
  `auth_token` varchar(200) DEFAULT NULL,
  `optimality_ALL` double DEFAULT '0',
  `optimality_DAY` double NOT NULL DEFAULT '0',
  `optimality_WEEK` double DEFAULT '0',
  `optimality_MONTH` double DEFAULT '0',
  `optimality_YEAR` double DEFAULT '0',
  `net_money_ALL` int(11) DEFAULT '0',
  `net_money_DAY` int(11) DEFAULT '0',
  `net_money_WEEK` int(11) DEFAULT '0',
  `net_money_MONTH` int(11) DEFAULT '0',
  `net_money_YEAR` int(11) DEFAULT '0',
  `rank_cache_data` date DEFAULT NULL,
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `auth_token_UNIQUE` (`auth_token`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8$$


