/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.22-71.0 : Database - perf-exchange
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`perf-exchange` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `perf-exchange`;

/*Table structure for table `e_app_version` */

DROP TABLE IF EXISTS `e_app_version`;

CREATE TABLE `e_app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appVersionNum` varchar(30) NOT NULL COMMENT '版本号',
  `platformType` varchar(1) NOT NULL DEFAULT '0' COMMENT '0Android',
  `updateContent_cn` text COMMENT '更新内容(中文)',
  `updateContent_en` text COMMENT '更新内容(英文)',
  `updateContent_hk` text COMMENT '更新内容( 繁体)',
  `updateWay` varchar(500) DEFAULT NULL,
  `updateLink` varchar(500) DEFAULT NULL COMMENT '更新渠道',
  `isMustUpdated` int(1) DEFAULT '0' COMMENT '0：非必须；1：必须',
  `publisher` varchar(255) DEFAULT NULL COMMENT '发布人',
  `releaseTime` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_app_version` */

/*Table structure for table `e_bind` */

DROP TABLE IF EXISTS `e_bind`;

CREATE TABLE `e_bind` (
  `bind_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `goldpay_id` varchar(255) DEFAULT NULL,
  `goldpay_name` varchar(255) DEFAULT NULL,
  `goldpay_acount` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='goldpay绑定';

/*Data for the table `e_bind` */

/*Table structure for table `e_config` */

DROP TABLE IF EXISTS `e_config`;

CREATE TABLE `e_config` (
  `config_key` varchar(255) NOT NULL DEFAULT '0' COMMENT '配置键',
  `config_value` varchar(255) DEFAULT NULL COMMENT '配置的值',
  `config_name` varchar(255) DEFAULT NULL COMMENT '配置名字',
  `config_order` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `config_canChange` int(1) NOT NULL DEFAULT '0' COMMENT '是否在管理端可更改;0:不可;1:可',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置';

/*Data for the table `e_config` */

insert  into `e_config`(`config_key`,`config_value`,`config_name`,`config_order`,`config_canChange`) values ('change_phone_time','15','换绑手机间隔（DAY）',6,1),('daily_transfer_threshold','10','短信验证每日转账最大阈值（美元）',2,1),('download_link','http://t.cn/RMzEnuJ','APP下载链接',9,0),('each_transfer_threshold','1','短信验证每笔转账阈值（美元）',3,1),('enter_maximum_amount','1000000000','最大输入',4,1),('refund_time','7','邀请转账过期时间（DAY）',7,1),('reserve_funds','120','预备金金额（GDQ,仅限整数）',8,1),('total_balance_threshold','1000','短信验证总账阈值（美元）',1,1),('tpps_client_id','485611e97116447ab1a36bd4c1275d8d','第三方支付商户ID',10,0),('tpps_client_key','8ad351c24602b2c7ae7f30b52864ecb0','第三方支付商户Key',11,0),('tpps_trans_token','2426ae83711949d3be70f12f6e1d9c49','第三方支付商户Token',12,0),('verify_time','10','验证码过期时间（MIN）',5,1);

/*Table structure for table `e_crm_admin` */

DROP TABLE IF EXISTS `e_crm_admin`;

CREATE TABLE `e_crm_admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `admin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
  `admin_password` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录密码',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员';

/*Data for the table `e_crm_admin` */

insert  into `e_crm_admin`(`admin_id`,`admin_name`,`admin_password`,`password_salt`) values (1,'admin','05c6c3f4129c9398aa58e233533b4499','31f2aa6d1c0c3eff354a9c85f638faab');

/*Table structure for table `e_crm_alarm` */

DROP TABLE IF EXISTS `e_crm_alarm`;

CREATE TABLE `e_crm_alarm` (
  `alarm_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alarm_type` int(1) DEFAULT '0' COMMENT '报警类型，暂无其他均为0',
  `alarm_grade` varchar(255) DEFAULT NULL COMMENT '报警等级',
  `lower_limit` decimal(20,4) DEFAULT NULL COMMENT '下限',
  `upper_limit` decimal(20,4) DEFAULT NULL COMMENT '上限',
  `alarm_mode` int(11) DEFAULT '1' COMMENT '报警方式 1：短信2：邮件3短信+邮件',
  `supervisor_id_arr` varchar(255) DEFAULT NULL COMMENT '存储相关人员的id',
  `alarm_available` int(1) DEFAULT NULL COMMENT '报警可用，0false1true',
  `create_at` datetime DEFAULT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '编辑人id',
  PRIMARY KEY (`alarm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_crm_alarm` */

/*Table structure for table `e_crm_supervisor` */

DROP TABLE IF EXISTS `e_crm_supervisor`;

CREATE TABLE `e_crm_supervisor` (
  `supervisor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supervisor_name` varchar(255) DEFAULT NULL COMMENT '监督人姓名',
  `supervisor_mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `supervisor_email` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`supervisor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_crm_supervisor` */

/*Table structure for table `e_crm_user_info` */

DROP TABLE IF EXISTS `e_crm_user_info`;

CREATE TABLE `e_crm_user_info` (
  `user_id` bigint(20) NOT NULL,
  `area_code` varchar(10) DEFAULT NULL COMMENT '区号',
  `user_phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `user_type` int(1) DEFAULT NULL COMMENT '用户类型',
  `user_available` int(1) DEFAULT NULL COMMENT '用户是否冻结',
  `user_total_assets` decimal(20,4) DEFAULT NULL COMMENT '用户总资产',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_crm_user_info` */

/*Table structure for table `e_currency` */

DROP TABLE IF EXISTS `e_currency`;

CREATE TABLE `e_currency` (
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `name_en` varchar(255) DEFAULT NULL COMMENT '英文名字',
  `name_cn` varchar(255) DEFAULT NULL COMMENT '简体名字',
  `name_hk` varchar(255) DEFAULT NULL COMMENT '繁体名字',
  `currency_unit` varchar(255) DEFAULT NULL COMMENT '货币单位',
  `currency_status` int(1) NOT NULL DEFAULT '0' COMMENT '币种状态  0：下架；1：上架',
  `currency_order` int(5) DEFAULT NULL COMMENT '货币顺序',
  PRIMARY KEY (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='币种';

/*Data for the table `e_currency` */

insert  into `e_currency`(`currency`,`name_en`,`name_cn`,`name_hk`,`currency_unit`,`currency_status`,`currency_order`) values ('CNY','CNY','人民币','人民幣','CNY',1,2),('EUR','EUR','欧元','歐元','EUR',1,4),('GBP','GBP','英镑','英鎊','GBP',1,5),('GDQ','GDQ','沛金条','沛金條','Q',1,1),('HKD','HKD','港币','港幣','HKD',1,7),('JPY','JPY','日元','日元','JPY',1,6),('USD','USD','美元','美元','USD',1,3);

/*Table structure for table `e_exchange` */

DROP TABLE IF EXISTS `e_exchange`;

CREATE TABLE `e_exchange` (
  `exchange_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '兑换ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency_out` char(3) NOT NULL DEFAULT '' COMMENT '兑出币种',
  `currency_in` char(3) NOT NULL DEFAULT '' COMMENT '兑入币种',
  `amount_out` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '兑出金额',
  `amount_in` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '兑入金额',
  `exchange_rate` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '汇率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `exchange_status` int(1) NOT NULL DEFAULT '0' COMMENT '兑换状态',
  PRIMARY KEY (`exchange_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换记录';

/*Data for the table `e_exchange` */

/*Table structure for table `e_friend` */

DROP TABLE IF EXISTS `e_friend`;

CREATE TABLE `e_friend` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `friend_Id` int(11) NOT NULL DEFAULT '0' COMMENT '好友的用户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`friend_Id`),
  KEY `FK_friend_id` (`friend_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友';

/*Data for the table `e_friend` */

/*Table structure for table `e_transaction_notification` */

DROP TABLE IF EXISTS `e_transaction_notification`;

CREATE TABLE `e_transaction_notification` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT,
  `sponsor_id` int(11) DEFAULT NULL COMMENT '发起人Id',
  `payer_id` int(11) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL COMMENT '币种',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '交易金额',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `notice_status` int(255) DEFAULT NULL COMMENT '通知状态',
  `trading_status` int(11) DEFAULT NULL COMMENT '交易状态',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_transaction_notification` */

/*Table structure for table `e_transfer` */

DROP TABLE IF EXISTS `e_transfer`;

CREATE TABLE `e_transfer` (
  `transfer_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '转账ID',
  `user_from` int(11) NOT NULL DEFAULT '0' COMMENT '转出账户',
  `user_to` int(11) NOT NULL DEFAULT '0' COMMENT '转入账户',
  `area_code` varchar(255) DEFAULT '',
  `phone` varchar(255) DEFAULT '' COMMENT '参与者手机',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `transfer_amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '转账金额',
  `transfer_comment` varchar(255) DEFAULT NULL COMMENT '转账留言',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `transfer_status` int(1) NOT NULL DEFAULT '0' COMMENT '转账状态',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转账OR充值',
  `notice_id` int(255) DEFAULT NULL COMMENT '通知消息id',
  PRIMARY KEY (`transfer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录';

/*Data for the table `e_transfer` */

/*Table structure for table `e_unregistered` */

DROP TABLE IF EXISTS `e_unregistered`;

CREATE TABLE `e_unregistered` (
  `unregistered_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '转账金额',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `unregistered_status` int(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `transfer_id` varchar(255) NOT NULL DEFAULT '' COMMENT '转账ID',
  `refund_trans_id` varchar(255) DEFAULT NULL COMMENT '退款时交易号',
  PRIMARY KEY (`unregistered_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='未注册用户金额';

/*Data for the table `e_unregistered` */

/*Table structure for table `e_user` */

DROP TABLE IF EXISTS `e_user`;

CREATE TABLE `e_user` (
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
  `push_id` varchar(255) DEFAULT NULL COMMENT '设备号',
  `push_tag` varchar(255) NOT NULL DEFAULT '0' COMMENT '语言',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

/*Data for the table `e_user` */

insert  into `e_user`(`user_id`,`area_code`,`user_phone`,`user_name`,`user_password`,`user_pay_pwd`,`create_time`,`login_time`,`login_ip`,`user_type`,`user_available`,`password_salt`,`push_id`,`push_tag`) values (1,'+86','9999999','system','',NULL,'2017-01-10 19:04:09',NULL,NULL,1,0,'',NULL,'0');

/*Table structure for table `e_user_config` */

DROP TABLE IF EXISTS `e_user_config`;

CREATE TABLE `e_user_config` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_config_value` text COMMENT '配置的值',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户配置表';

/*Data for the table `e_user_config` */

/*Table structure for table `e_wallet` */

DROP TABLE IF EXISTS `e_wallet`;

CREATE TABLE `e_wallet` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  PRIMARY KEY (`wallet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='钱包';

/*Data for the table `e_wallet` */

insert  into `e_wallet`(`wallet_id`,`user_id`,`currency`,`balance`,`update_time`) values (1,1,'CNY','0.0000','2017-01-22 03:42:07'),(2,1,'USD','0.0000','2017-01-20 05:28:15'),(3,1,'HKD','0.0000','2017-01-20 05:29:04'),(4,1,'EUR','0.0000','2017-01-20 05:28:23'),(5,1,'GBP','0.0000','2017-01-20 05:28:43'),(6,1,'JPY','0.0000','2017-01-20 05:28:55'),(7,1,'GDQ','0.0000','2017-01-22 03:43:06');

/*Table structure for table `e_wallet_seq` */

DROP TABLE IF EXISTS `e_wallet_seq`;

CREATE TABLE `e_wallet_seq` (
  `seq_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转入OR转出',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '金额',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`seq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流水';

/*Data for the table `e_wallet_seq` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
