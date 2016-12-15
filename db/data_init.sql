/*
SQLyog Community Edition- MySQL GUI v6.07
Host - 5.6.19-0ubuntu0.14.04.1 : Database - anytime_exchange
*********************************************************************
Server version : 5.6.19-0ubuntu0.14.04.1
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Data for the table `config` */

insert  into `config`(`config_key`,`config_value`) values ('standard_currency','USD');

/*Data for the table `currency` */

insert  into `currency`(`currency`,`name_en`,`name_cn`,`name_hk`,`currency_image`,`currency_status`,`transfer_max`,`transfer_large`,`asset_threshold`) values ('CNY','CNY','CNY','CNY','http://image.baidu.com/',1,NULL,NULL,NULL),('EUR','EUR','EUR','EUR','http://image.baidu.com/',1,NULL,NULL,NULL),('GBP','GBP','GBP','GBP','http://image.baidu.com/',1,NULL,NULL,NULL),('HKD','HKD','HKD','HKD','http://image.baidu.com/',1,NULL,NULL,NULL),('JPY','JPY','JPY','JPY','http://image.baidu.com/',1,NULL,NULL,NULL),('USD','USD','USD','USD','http://image.baidu.com/',1,'10000.00','10000.00','10000.00');

/*Data for the table `user` */

insert  into `user`(`user_id`,`area_code`,`user_phone`,`user_name`,`user_password`,`user_pay_pwd`,`create_time`,`login_time`,`login_ip`,`user_type`,`user_available`,`password_salt`) values (1,'','12345698741','system','',NULL,'2016-12-01 14:15:44','2016-12-01 14:11:54',NULL,1,0,'');

/*Data for the table `wallet` */

insert  into `wallet`(`wallet_id`,`user_id`,`currency`,`balance`,`update_time`) values (1,1,'CNY','1000010.00','2016-12-14 18:05:40'),(2,1,'USD','999998.55','2016-12-06 14:18:07'),(3,1,'HKD','1000000.00','2016-12-01 13:59:54'),(4,1,'EUR','1000000.00','2016-12-01 14:00:25'),(5,1,'GBP','1000000.00','2016-12-01 14:00:45'),(6,1,'JPY','1000000.00','2016-12-01 14:01:11'),(7,1,'GDP','1000000.00','2016-12-01 14:01:59');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
