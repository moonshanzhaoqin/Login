# Host: 10.1.1.66  (Version: 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-01-10 12:13:12
# Generator: MySQL-Front 5.3  (Build 4.81)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "e_app_version"
#

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for table "e_bind"
#

DROP TABLE IF EXISTS `e_bind`;
CREATE TABLE `e_bind` (
  `bind_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `goldpay_id` varchar(255) DEFAULT NULL,
  `goldpay_name` varchar(255) DEFAULT NULL,
  `goldpay_acount` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='goldpay绑定';

#
# Structure for table "e_config"
#

DROP TABLE IF EXISTS `e_config`;
CREATE TABLE `e_config` (
  `config_key` varchar(255) NOT NULL DEFAULT '0' COMMENT '配置键',
  `config_value` varchar(255) DEFAULT NULL COMMENT '配置的值',
  `config_name` varchar(255) DEFAULT NULL COMMENT '配置名字',
  `config_order` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置';

#
# Structure for table "e_crm_admin"
#

DROP TABLE IF EXISTS `e_crm_admin`;
CREATE TABLE `e_crm_admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `admin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
  `admin_password` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录密码',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='管理员';

#
# Structure for table "e_crm_alarm"
#

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

#
# Structure for table "e_crm_supervisor"
#

DROP TABLE IF EXISTS `e_crm_supervisor`;
CREATE TABLE `e_crm_supervisor` (
  `supervisor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supervisor_name` varchar(255) DEFAULT NULL COMMENT '监督人姓名',
  `supervisor_mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `supervisor_email` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`supervisor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

#
# Structure for table "e_crm_user_info"
#

DROP TABLE IF EXISTS `e_crm_user_info`;
CREATE TABLE `e_crm_user_info` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `area_code` varchar(5) DEFAULT NULL COMMENT '区号',
  `user_phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `user_type` int(1) DEFAULT NULL COMMENT '用户类型',
  `user_available` int(1) DEFAULT NULL COMMENT '用户是否冻结',
  `user_total_assets` decimal(20,4) DEFAULT NULL COMMENT '用户总资产',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "e_currency"
#

DROP TABLE IF EXISTS `e_currency`;
CREATE TABLE `e_currency` (
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `name_en` varchar(255) DEFAULT NULL COMMENT '英文名字',
  `name_cn` varchar(255) DEFAULT NULL COMMENT '简体名字',
  `name_hk` varchar(255) DEFAULT NULL COMMENT '繁体名字',
  `currency_unit` varchar(255) DEFAULT NULL COMMENT '货币单位',
  `currency_status` int(1) NOT NULL DEFAULT '0' COMMENT '币种状态 0:下架；1:上架',
  `currency_order` int(11) unsigned DEFAULT '0' COMMENT '货币顺序',
  PRIMARY KEY (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='币种';

#
# Structure for table "e_exchange"
#

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

#
# Structure for table "e_transaction_notification"
#

DROP TABLE IF EXISTS `e_transaction_notification`;
CREATE TABLE `e_transaction_notification` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT,
  `sponsor_id` int(11) DEFAULT NULL COMMENT '发起人Id',
  `payer_id` int(11) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL COMMENT '币种',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '交易金额',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `notice_status` int(1) DEFAULT NULL COMMENT '通知状态',
  `trading_status` int(1) DEFAULT NULL COMMENT '交易状态',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

#
# Structure for table "e_transfer"
#

DROP TABLE IF EXISTS `e_transfer`;
CREATE TABLE `e_transfer` (
  `transfer_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '转账ID',
  `user_from` int(11) NOT NULL DEFAULT '0' COMMENT '转出账户',
  `user_to` int(11) NOT NULL DEFAULT '0' COMMENT '转入账户',
  `area_code` varchar(5) DEFAULT '',
  `phone` varchar(255) DEFAULT '' COMMENT '参与者手机',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `transfer_amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '转账金额',
  `transfer_comment` varchar(255) DEFAULT NULL COMMENT '转账留言',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `transfer_status` int(1) NOT NULL DEFAULT '0' COMMENT '转账状态',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转账OR充值',
  `notice_id` int(11) DEFAULT NULL COMMENT '通知消息id',
  PRIMARY KEY (`transfer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录';

#
# Structure for table "e_unregistered"
#

DROP TABLE IF EXISTS `e_unregistered`;
CREATE TABLE `e_unregistered` (
  `unregistered_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种名称',
  `amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '转账金额',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `unregistered_status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:未取 1:退回 2:取回)',
  `transfer_id` varchar(255) NOT NULL DEFAULT '' COMMENT '转账ID',
  `refund_trans_id` varchar(255) DEFAULT NULL COMMENT '退款时交易号',
  PRIMARY KEY (`unregistered_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='未注册用户金额';

#
# Structure for table "e_user"
#

DROP TABLE IF EXISTS `e_user`;
CREATE TABLE `e_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `user_name` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_pay_pwd` varchar(255) DEFAULT NULL COMMENT '用户支付密码',
  `create_time` datetime NOT NULL COMMENT '用户注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `login_ip` varchar(255) DEFAULT NULL COMMENT '最近登录IP',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型(0:普通 1:系统)',
  `user_available` int(1) NOT NULL DEFAULT '1' COMMENT '冻结标识 0:冻结 1:正常',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值(用户密码,支付密码)',
  `push_id` varchar(255) DEFAULT NULL COMMENT '设备号',
  `push_tag` int(2) NOT NULL DEFAULT '0' COMMENT '语言',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='用户';

#
# Structure for table "e_friend"
#

DROP TABLE IF EXISTS `e_friend`;
CREATE TABLE `e_friend` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `friend_id` int(11) NOT NULL DEFAULT '0' COMMENT '好友的用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`friend_id`),
  KEY `FK_friend_id` (`friend_id`),
  CONSTRAINT `FK_friend_id` FOREIGN KEY (`friend_Id`) REFERENCES `e_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友';

#
# Structure for table "e_wallet"
#

DROP TABLE IF EXISTS `e_wallet`;
CREATE TABLE `e_wallet` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',
  PRIMARY KEY (`wallet_id`),
  KEY `currency` (`currency`),
  CONSTRAINT `e_wallet_ibfk_1` FOREIGN KEY (`currency`) REFERENCES `e_currency` (`currency`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 COMMENT='钱包';

#
# Structure for table "e_wallet_seq"
#

DROP TABLE IF EXISTS `e_wallet_seq`;
CREATE TABLE `e_wallet_seq` (
  `seq_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `transfer_type` int(1) NOT NULL DEFAULT '0' COMMENT '转入OR转出',
  `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '金额',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`seq_id`)
) ENGINE=InnoDB AUTO_INCREMENT=451 DEFAULT CHARSET=utf8 COMMENT='流水';
