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

/*Table structure for table `app_version` */

DROP TABLE IF EXISTS `app_version`;

CREATE TABLE `app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appVersionNum` varchar(30) NOT NULL COMMENT '版本号',
  `platformType` varchar(1) NOT NULL DEFAULT '0' COMMENT '0Android',
  `updateContent` text COMMENT '更新内容',
  `updateWay` varchar(500) DEFAULT NULL,
  `updateLink` varchar(500) DEFAULT NULL COMMENT '更新渠道',
  `isMustUpdated` int(1) DEFAULT '0' COMMENT '0：必须；1：非必须',
  `publisher` varchar(255) DEFAULT NULL COMMENT '发布人',
  `releaseTime` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `bind` */

DROP TABLE IF EXISTS `bind`;

CREATE TABLE `bind` (
  `bind_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `goldpay_id` varchar(255) DEFAULT NULL,
  `goldpay_name` varchar(255) DEFAULT NULL,
  `goldpay_acount` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='goldpay绑定';

/*Table structure for table `config` */

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
  `config_key` varchar(255) NOT NULL DEFAULT '0',
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置';

/*Table structure for table `currency` */

DROP TABLE IF EXISTS `currency`;

CREATE TABLE `currency` (
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `name_en` varchar(255) DEFAULT NULL COMMENT '英文名字',
  `name_cn` varchar(255) DEFAULT NULL COMMENT '简体名字',
  `name_hk` varchar(255) DEFAULT NULL COMMENT '繁体名字',
  `currency_image` varchar(255) DEFAULT NULL COMMENT '币种图片',
  `currency_status` int(1) NOT NULL DEFAULT '0' COMMENT '币种状态  0：下架；1：上架',
  `transfer_max` decimal(10,2) DEFAULT NULL COMMENT '每日转账最大限额',
  `transfer_large` decimal(10,2) DEFAULT NULL COMMENT '单次转账大额阈值',
  `asset_threshold` decimal(10,2) DEFAULT NULL COMMENT '总资产阈值',
  PRIMARY KEY (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='币种';

/*Table structure for table `exchange` */

DROP TABLE IF EXISTS `exchange`;

CREATE TABLE `exchange` (
  `exchange_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '兑换ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency_out` char(3) NOT NULL DEFAULT '' COMMENT '兑出币种',
  `currency_in` char(3) NOT NULL DEFAULT '' COMMENT '兑入币种',
  `amount_out` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '兑出金额',
  `amount_in` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '兑入金额',
  `exchange_rate` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '汇率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `exchange_status` int(1) NOT NULL DEFAULT '0' COMMENT '兑换状态',
  PRIMARY KEY (`exchange_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换记录';

/*Table structure for table `friend` */

DROP TABLE IF EXISTS `friend`;

CREATE TABLE `friend` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `friend_Id` int(11) NOT NULL DEFAULT '0' COMMENT '好友的用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`friend_Id`),
  KEY `FK_friend_id` (`friend_Id`),
  CONSTRAINT `FK_friend_id` FOREIGN KEY (`friend_Id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友';

/*Table structure for table `transfer` */

DROP TABLE IF EXISTS `transfer`;

CREATE TABLE `transfer` (
  `transfer_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '转账ID',
  `user_from` int(11) NOT NULL DEFAULT '0' COMMENT '转出账户',
  `user_to` int(11) NOT NULL DEFAULT '0' COMMENT '转入账户',
  `area_code` varchar(255) DEFAULT '',
  `phone` varchar(255) DEFAULT '' COMMENT '参与者手机',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `transfer_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '转账金额',
  `transfer_comment` varchar(255) DEFAULT NULL COMMENT '转账留言',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `transfer_status` int(1) NOT NULL DEFAULT '0' COMMENT '转账状态',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转账OR充值',
  `notice_id` int(255) DEFAULT NULL COMMENT '通知消息id',
  PRIMARY KEY (`transfer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录';

/*Table structure for table `unregistered` */

DROP TABLE IF EXISTS `unregistered`;

CREATE TABLE `unregistered` (
  `unregistered_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '转账金额',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `unregistered_status` int(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `transfer_id` varchar(255) NOT NULL DEFAULT '' COMMENT '转账ID',
  PRIMARY KEY (`unregistered_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='未注册用户金额';

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `user_name` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_pay_pwd` varchar(255) DEFAULT NULL COMMENT '用户支付密码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户注册时间',
  `login_time` timestamp NULL DEFAULT NULL COMMENT '最近登录时间',
  `login_ip` varchar(255) DEFAULT NULL COMMENT '最近登录IP',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型',
  `user_available` int(1) NOT NULL DEFAULT '1' COMMENT '冻结标识 0:冻结 1:正常',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值（用户密码，支付密码）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

/*Table structure for table `wallet` */

DROP TABLE IF EXISTS `wallet`;

CREATE TABLE `wallet` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  PRIMARY KEY (`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包';

/*Table structure for table `wallet_seq` */

DROP TABLE IF EXISTS `wallet_seq`;

CREATE TABLE `wallet_seq` (
  `seq_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转入OR转出',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  PRIMARY KEY (`seq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流水';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
