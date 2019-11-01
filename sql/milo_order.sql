/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.25-log : Database - milo_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`milo_order` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `milo_order`;

/*Table structure for table `tbl_order` */

DROP TABLE IF EXISTS `tbl_order`;

CREATE TABLE `tbl_order` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `count` int(11) NOT NULL COMMENT '购买的件数',
  `amount` decimal(10,0) NOT NULL COMMENT '订单的总金额',
  `status` int(11) NOT NULL COMMENT '订单的状态 1:未支付 2:支付中 3:支付失败 4:支付成功',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbl_order` */

insert  into `tbl_order`(`id`,`user_id`,`product_id`,`count`,`amount`,`status`,`create_time`,`update_time`) values ('dc39e91120118af3e6397059a67b229e','001','1',7,'70',4,'2019-11-01 15:26:38','2019-11-01 15:26:50'),('d391bba9c0176b4518ffb9d3e4b38477','001','1',7,'70',4,'2019-11-01 15:35:11','2019-11-01 15:35:11'),('22402ce966d0466c0af80562de985d48','001','1',7,'70',4,'2019-11-01 15:35:16','2019-11-01 15:35:16'),('a919ecc36829966978b3470144803cef','001','1',7,'70',4,'2019-11-01 15:35:17','2019-11-01 15:35:17'),('fc397f88c0a037704b031d1717add078','001','1',7,'70',4,'2019-11-01 17:09:01','2019-11-01 17:09:24'),('b023cc094551094d4b83be2366ff1a34','001','1',7,'70',4,'2019-11-01 17:15:52','2019-11-01 17:15:52'),('d8375f7be1308182215f0f0b935cf3a9','001','1',7,'70',4,'2019-11-01 17:16:05','2019-11-01 17:16:14'),('ed03019854008a2b28ce74f457e7f550','001','1',7,'70',4,'2019-11-01 17:18:03','2019-11-01 17:18:03'),('46c752390cf1586c9431fa26c9fabea4','001','1',7,'70',4,'2019-11-01 17:25:47','2019-11-01 17:26:01'),('01375bb1ff2ddc24f6fe4052b93c3020','001','1',7,'70',4,'2019-11-01 17:26:16','2019-11-01 17:26:18'),('452ce908f4835bc30fcf12fc6594e56b','001','1',7,'70',4,'2019-11-01 17:26:26','2019-11-01 17:26:28'),('30af5251bcc7fcde04d1dff692841a10','001','1',7,'70',4,'2019-11-01 17:26:53','2019-11-01 17:26:58'),('e2b0fb6554804f22b1d08d20a2b4935c','001','1',7,'70',4,'2019-11-01 17:27:08','2019-11-01 17:27:14'),('5c1e5f59376fe1827f4d41c2287a4307','001','1',7,'70',4,'2019-11-01 17:28:16','2019-11-01 17:28:22'),('f8e675fb44ee8f8fa8d5b6c3ff6ee483','001','1',7,'70',4,'2019-11-01 17:31:35','2019-11-01 17:31:35'),('a023ffbb688c60668004041319b51a2c','001','1',1,'10',4,'2019-11-01 17:32:25','2019-11-01 17:32:25'),('15c6dcfbdb0487fd2d0f7a4234d0fea0','001','1',1,'10',4,'2019-11-01 17:32:43','2019-11-01 17:32:43'),('dfce493ae149c43d4d8b46aaef7f22b9','001','1',1,'10',4,'2019-11-01 17:33:38','2019-11-01 17:33:38'),('62886e9a60c961410d8029c7884930ed','001','1',1,'10',4,'2019-11-01 17:33:44','2019-11-01 17:33:46'),('f5c247ae3d473229ce3c160aff547530','001','1',1,'10',4,'2019-11-01 17:35:04','2019-11-01 17:35:11'),('3f44c7b4adce8aa9396b8989c5ab2f35','001','1',1,'10',4,'2019-11-01 17:37:16','2019-11-01 17:37:19');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
