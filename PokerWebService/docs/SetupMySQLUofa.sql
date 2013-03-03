delimiter $$

USE `ece493grp2`$$

delimiter $$

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
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameUUID` varchar(45) NOT NULL,
  `date_uploaded` date NOT NULL,
  PRIMARY KEY (`id`,`gameUUID`),
  UNIQUE KEY `gameUUID_UNIQUE` (`gameUUID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `misc_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` blob NOT NULL,
  `date_uploaded` date NOT NULL,
  PRIMARY KEY (`id`,`accountID`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `user_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `password` mediumtext NOT NULL,
  `auth_token` varchar(200) DEFAULT NULL,
  `delta_money` int(11) DEFAULT '0',
  `optimality` double DEFAULT '0',
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `auth_token_UNIQUE` (`auth_token`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8$$


