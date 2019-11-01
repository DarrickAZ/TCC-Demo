/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.25-log : Database - milo_tcc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`milo_tcc` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `milo_tcc`;

/*Table structure for table `milo_account_log` */

DROP TABLE IF EXISTS `milo_account_log`;

CREATE TABLE `milo_account_log` (
  `trans_id` varchar(64) NOT NULL,
  `phase` tinyint(4) NOT NULL,
  `role` tinyint(4) NOT NULL,
  `retried_times` tinyint(4) NOT NULL,
  `version` tinyint(4) NOT NULL,
  `target_class` varchar(256) DEFAULT NULL,
  `target_method` varchar(128) DEFAULT NULL,
  `confirm_method` varchar(128) DEFAULT NULL,
  `cancel_method` varchar(128) DEFAULT NULL,
  `invocation` longblob,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `milo_account_log` */

/*Table structure for table `milo_order_log` */

DROP TABLE IF EXISTS `milo_order_log`;

CREATE TABLE `milo_order_log` (
  `trans_id` varchar(64) NOT NULL,
  `phase` tinyint(4) NOT NULL,
  `role` tinyint(4) NOT NULL,
  `retried_times` tinyint(4) NOT NULL,
  `version` tinyint(4) NOT NULL,
  `target_class` varchar(256) DEFAULT NULL,
  `target_method` varchar(128) DEFAULT NULL,
  `confirm_method` varchar(128) DEFAULT NULL,
  `cancel_method` varchar(128) DEFAULT NULL,
  `invocation` longblob,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `milo_order_log` */

/*Table structure for table `milo_stock_log` */

DROP TABLE IF EXISTS `milo_stock_log`;

CREATE TABLE `milo_stock_log` (
  `trans_id` varchar(64) NOT NULL,
  `phase` tinyint(4) NOT NULL,
  `role` tinyint(4) NOT NULL,
  `retried_times` tinyint(4) NOT NULL,
  `version` tinyint(4) NOT NULL,
  `target_class` varchar(256) DEFAULT NULL,
  `target_method` varchar(128) DEFAULT NULL,
  `confirm_method` varchar(128) DEFAULT NULL,
  `cancel_method` varchar(128) DEFAULT NULL,
  `invocation` longblob,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `milo_stock_log` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
