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

/*Table structure for table `e_bad_account` */

DROP TABLE IF EXISTS `e_bad_account`;

CREATE TABLE `e_bad_account` (
  `bad_account_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `currency` char(3) NOT NULL DEFAULT '',
  `sum_amount` decimal(20,4) NOT NULL DEFAULT '0.0000',
  `balance_history` decimal(20,4) NOT NULL DEFAULT '0.0000',
  `balance_now` decimal(20,4) NOT NULL DEFAULT '0.0000',
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `start_seq_id` bigint(20) NOT NULL DEFAULT '0',
  `end_seq_id` bigint(20) NOT NULL DEFAULT '0',
  `bad_account_status` int(1) NOT NULL DEFAULT '0',
  `transfer_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`bad_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='坏账记录';

/*Data for the table `e_bad_account` */

/*Table structure for table `e_bind` */

DROP TABLE IF EXISTS `e_bind`;

CREATE TABLE `e_bind` (
  `bind_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `goldpay_id` varchar(255) DEFAULT NULL,
  `goldpay_name` varchar(255) DEFAULT NULL,
  `goldpay_acount` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bind_id`),
  UNIQUE KEY `index_user_id` (`user_id`)
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

insert  into `e_config`(`config_key`,`config_value`,`config_name`,`config_order`,`config_canChange`) values ('change_phone_time','7','换绑手机间隔（天）',12,1),('daily_transfer_threshold','500','短信验证每日转账最大阈值（美元）',7,1),('download_link','http://t.cn/RiivSMr','APP下载链接',4,0),('each_transfer_threshold','10','短信验证每笔转账阈值（美元）',8,1),('enter_maximum_amount','1000000000','最大输入',9,1),('exchange_fee','0','兑换手续费（千分比）',23,0),('exchange_limit_daily_pay','1000000','每天兑换金额限制（美元）',21,1),('exchange_limit_each_time','100000','每次兑换金额限制（美元）',20,1),('exchange_limit_number_of_pay_per_day','10','每天兑换次数限制 （次）',22,1),('login_unavailiable_time','1','登录冻结时间（小时）',16,1),('paypal_accessToken','access_token$sandbox$h32wtjg3dw3jt4kd$b070893b51856e786beb87fc90865823','PayPal_accessToken',0,0),('paypal_expiration','600','PayPal支付过期时间(秒)',0,0),('paypal_max_limit_each_time','30000','PayPal单笔交易金额上限',0,1),('paypal_mini_limit_each_time','90','PayPal单笔交易金额下限',0,1),('paypal_recharge','true','开启PayPal充值Goldpay',0,1),('pay_unavailiable_time','1','支付冻结时间（小时）',14,1),('refund_time','7','邀请转账过期时间（天）',10,1),('reserve_funds','50000','预备金金额（GDQ,仅限整数）',5,1),('total_balance_threshold','50000','短信验证总账阈值（美元）',6,1),('total_gdq_can_be_sold','320000','可售沛金条总量',0,1),('tpps_client_id','b4c9c6c9ae784c3297d6cebf99e2ca47','第三方支付商户ID',1,0),('tpps_client_key','fff2226ca87fa0d871278a9b43945b9b','第三方支付商户Key',2,0),('tpps_trans_token','dcd8e32afef04989953967deeedae014','第三方支付商户Token',3,0),('transfer_limit_daily_pay','1000000','每天支付金额限制（美元）',18,1),('transfer_limit_each_time','100000','每次支付金额限制（美元）',17,1),('transfer_limit_number_of_pay_per_day','10','每天支付次数限制 （次）',19,1),('verify_time','10','验证码过期时间（分钟）',11,1),('wrong_password_frequency','5','错误登录密码次数（次）',15,1),('wrong_paypwd_frequency','5','错误支付密码次数（次）',13,1);

/*Table structure for table `e_crm_admin` */

DROP TABLE IF EXISTS `e_crm_admin`;

CREATE TABLE `e_crm_admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `admin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
  `admin_password` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录密码',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值',
  `admin_power` varchar(255) DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员';

/*Data for the table `e_crm_admin` */

insert  into `e_crm_admin`(`admin_id`,`admin_name`,`admin_password`,`password_salt`,`admin_power`) values (1,'admin','05c6c3f4129c9398aa58e233533b4499','31f2aa6d1c0c3eff354a9c85f638faab','0,1,2,3,4,5,6,7');

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

/*Table structure for table `e_crm_log` */

DROP TABLE IF EXISTS `e_crm_log`;

CREATE TABLE `e_crm_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '操作时间',
  `operation` varchar(255) NOT NULL DEFAULT '0' COMMENT '操作',
  `target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员日志';

/*Data for the table `e_crm_log` */

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
  `create_time` datetime DEFAULT NULL,
  `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `user_type` int(1) DEFAULT NULL COMMENT '用户类型',
  `user_available` int(1) DEFAULT NULL COMMENT '用户是否冻结',
  `login_available` int(1) NOT NULL DEFAULT '1' COMMENT '登录冻结标识 0:冻结 1:正常',
  `pay_available` int(1) NOT NULL DEFAULT '1' COMMENT '支付冻结标识 0:冻结 1:正常',
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

insert  into `e_currency`(`currency`,`name_en`,`name_cn`,`name_hk`,`currency_unit`,`currency_status`,`currency_order`) values ('CNY','CNY','人民币','人民幣','CNY',1,2),('EUR','EUR','欧元','歐元','EUR',1,3),('GBP','GBP','英镑','英鎊','GBP',1,4),('GDQ','GDQ','沛金条','沛金條','Q',1,0),('HKD','HKD','港币','港幣','HKD',1,6),('JPY','JPY','日元','日元','JPY',1,5),('USD','USD','美元','美元','USD',1,1);

/*Table structure for table `e_exchange` */

DROP TABLE IF EXISTS `e_exchange`;

CREATE TABLE `e_exchange` (
  `exchange_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '兑换ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency_out` char(3) NOT NULL DEFAULT '' COMMENT '兑出币种',
  `currency_in` char(3) NOT NULL DEFAULT '' COMMENT '兑入币种',
  `amount_out` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '兑出金额',
  `amount_in` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '兑入金额',
  `exchange_rate` decimal(20,10) DEFAULT '0.0000000000',
  `exchange_fee_per_thousand` decimal(20,4) NOT NULL COMMENT '手续费千分比',
  `exchange_fee_amount` decimal(20,4) NOT NULL COMMENT '实际扣除手续费',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `exchange_status` int(1) NOT NULL DEFAULT '0' COMMENT '兑换状态',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT 'version',
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
  `version` int(11) NOT NULL DEFAULT '0' COMMENT 'version',
  `goldpay_result` varchar(255) DEFAULT NULL COMMENT 'goldpay回包',
  `goldpay_name` varchar(255) DEFAULT NULL,
  `goldpay_acount` varchar(255) DEFAULT NULL,
  `paypal_currency` char(3) DEFAULT '',
  `paypal_exchange` decimal(20,4) DEFAULT '0.0000',
  PRIMARY KEY (`transfer_id`),
  KEY `userfrom` (`user_from`),
  KEY `userto` (`user_to`)
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
  PRIMARY KEY (`unregistered_id`),
  UNIQUE KEY `index_transfer_id` (`transfer_id`)
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
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `login_time` timestamp NULL DEFAULT NULL COMMENT '最近登录时间',
  `login_ip` varchar(255) DEFAULT NULL COMMENT '最近登录IP',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型',
  `user_available` int(1) NOT NULL DEFAULT '1' COMMENT '冻结标识 0:冻结 1:正常',
  `login_available` int(1) NOT NULL DEFAULT '1' COMMENT '登录冻结标识 0:冻结 1:正常',
  `pay_available` int(1) NOT NULL DEFAULT '1' COMMENT '支付冻结标识 0:冻结 1:正常',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值（用户密码，支付密码）',
  `push_id` varchar(255) DEFAULT NULL COMMENT '设备号',
  `push_tag` varchar(255) NOT NULL DEFAULT '0' COMMENT '语言',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_area_phone` (`area_code`,`user_phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户';

/*Data for the table `e_user` */

insert  into `e_user`(`user_id`,`area_code`,`user_phone`,`user_name`,`user_password`,`user_pay_pwd`,`create_time`,`login_time`,`login_ip`,`user_type`,`user_available`,`login_available`,`pay_available`,`password_salt`,`push_id`,`push_tag`) values (1,'+86','9999999','system','',NULL,'2017-06-29 11:11:18',NULL,NULL,1,1,1,1,'',NULL,'0');

/*Table structure for table `e_user_config` */

DROP TABLE IF EXISTS `e_user_config`;

CREATE TABLE `e_user_config` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_config_value` text COMMENT '配置的值',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户配置表';

/*Data for the table `e_user_config` */

/*Table structure for table `e_user_device` */

DROP TABLE IF EXISTS `e_user_device`;

CREATE TABLE `e_user_device` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `device_id` varchar(255) NOT NULL DEFAULT '' COMMENT '设备ID',
  `device_name` varchar(255) NOT NULL DEFAULT '' COMMENT '设备名',
  PRIMARY KEY (`user_id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设备';

/*Data for the table `e_user_device` */

/*Table structure for table `e_wallet` */

DROP TABLE IF EXISTS `e_wallet`;

CREATE TABLE `e_wallet` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  `update_seq_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `userid_currency` (`user_id`,`currency`),
  KEY `index_update_time` (`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='钱包';

/*Data for the table `e_wallet` */

insert  into `e_wallet`(`wallet_id`,`user_id`,`currency`,`balance`,`update_time`,`update_seq_id`) values (1,1,'CNY','0.0000','2017-01-22 03:42:07',0),(2,1,'USD','0.0000','2017-01-20 05:28:15',0),(3,1,'HKD','0.0000','2017-01-20 05:29:04',0),(4,1,'EUR','0.0000','2017-01-20 05:28:23',0),(5,1,'GBP','0.0000','2017-01-20 05:28:43',0),(6,1,'JPY','0.0000','2017-01-20 05:28:55',0),(7,1,'GDQ','0.0000','2017-01-22 03:43:06',0);

/*Table structure for table `e_wallet_before` */

DROP TABLE IF EXISTS `e_wallet_before`;

CREATE TABLE `e_wallet_before` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  `update_seq_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`currency`),
  KEY `index_update_time` (`update_time`),
  KEY `index_update_seq_id` (`update_seq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包旧快照';

/*Data for the table `e_wallet_before` */

insert  into `e_wallet_before`(`user_id`,`currency`,`balance`,`update_time`,`update_seq_id`) values (1,'CNY','0.0000','2017-01-22 03:42:07',0),(1,'EUR','0.0000','2017-01-20 05:28:23',0),(1,'GBP','0.0000','2017-01-20 05:28:43',0),(1,'GDQ','0.0000','2017-01-22 03:43:06',0),(1,'HKD','0.0000','2017-01-20 05:29:04',0),(1,'JPY','0.0000','2017-01-20 05:28:55',0),(1,'USD','0.0000','2017-01-20 05:28:15',0);

/*Table structure for table `e_wallet_now` */

DROP TABLE IF EXISTS `e_wallet_now`;

CREATE TABLE `e_wallet_now` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  `update_seq_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`currency`),
  KEY `index_update_time` (`update_time`),
  KEY `index_update_seq_id` (`update_seq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包新快照';

/*Data for the table `e_wallet_now` */

/*Table structure for table `e_wallet_seq` */

DROP TABLE IF EXISTS `e_wallet_seq`;

CREATE TABLE `e_wallet_seq` (
  `seq_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转入OR转出',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '金额',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`seq_id`),
  KEY `index_user_id_currency` (`user_id`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流水';

/*Data for the table `e_wallet_seq` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
