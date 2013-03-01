delimiter $$

DROP DATABASE IF EXISTS `AndroidPoker`$$
CREATE DATABASE `AndroidPoker` /*!40100 DEFAULT CHARACTER SET utf8 */$$

USE `AndroidPoker`$$

DROP TABLE IF EXISTS `user_table`$$

CREATE TABLE `user_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` mediumtext NOT NULL,
  `password` mediumtext NOT NULL,
  `auth_token` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8$$


