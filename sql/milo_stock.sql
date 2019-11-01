/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.25-log : Database - milo_stock
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`milo_stock` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `milo_stock`;

/*Table structure for table `tbl_stock` */

DROP TABLE IF EXISTS `tbl_stock`;

CREATE TABLE `tbl_stock` (
  `id` varchar(32) NOT NULL,
  `product_name` varchar(20) NOT NULL,
  `total_stock` int(11) NOT NULL COMMENT '商品的总库存',
  `lock_stock` int(11) NOT NULL COMMENT '冻结的库存',
  `price` decimal(10,2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbl_stock` */

insert  into `tbl_stock`(`id`,`product_name`,`total_stock`,`lock_stock`,`price`,`create_time`,`update_time`) values ('1','多芬沐浴露',9882,7,'10.00','2019-11-01 15:00:17','2019-11-01 17:37:21'),('2','雕牌洗衣液',20000,0,'20.00','2019-11-01 15:00:52','2019-11-01 15:00:54'),('3','立白洗洁精',30000,0,'30.00','2019-11-01 15:01:17','2019-11-01 15:01:19');

/*Table structure for table `tbl_stock_lock` */

DROP TABLE IF EXISTS `tbl_stock_lock`;

CREATE TABLE `tbl_stock_lock` (
  `id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `order_id` varchar(32) NOT NULL,
  `lock_stock` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbl_stock_lock` */

insert  into `tbl_stock_lock`(`id`,`product_id`,`order_id`,`lock_stock`,`create_time`,`update_time`) values ('e2bb9da2133260c627bb8c5470c5090b','1','8ba9cf3268590ace729d8fbce1cb9b2f',7,'2019-11-01 17:09:17',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
