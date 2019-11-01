/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.25-log : Database - milo_account
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`milo_account` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `milo_account`;

/*Table structure for table `tbl_account` */

DROP TABLE IF EXISTS `tbl_account`;

CREATE TABLE `tbl_account` (
  `id` varchar(32) NOT NULL,
  `username` varchar(20) NOT NULL,
  `balance` decimal(10,2) NOT NULL COMMENT '用户的账户余额',
  `freeze_amount` decimal(10,2) NOT NULL COMMENT '账户的冻结总金额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbl_account` */

insert  into `tbl_account`(`id`,`username`,`balance`,`freeze_amount`,`create_time`,`update_time`) values ('001','zww','870.00','0.00','2019-11-01 14:57:55','2019-11-01 17:37:21'),('002','luke','2000.00','0.00','2019-11-01 14:58:36','2019-11-01 14:58:37'),('003','jack','3000.00','0.00','2019-11-01 14:58:56','2019-11-01 14:58:59');

/*Table structure for table `tbl_account_freeze` */

DROP TABLE IF EXISTS `tbl_account_freeze`;

CREATE TABLE `tbl_account_freeze` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `order_id` varchar(32) NOT NULL,
  `freeze_amount` decimal(10,2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbl_account_freeze` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
