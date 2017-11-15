# Host: 10.1.1.66  (Version: 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-01-05 10:01:19
# Generator: MySQL-Front 5.3  (Build 4.81)

/*!40101 SET NAMES utf8 */;

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
# Data for table "e_config"
#

INSERT INTO `e_config` VALUES ('change_phone_time','15','换绑手机期限（DAY）',6),('daily_transfer_threshold','10000','每日最大阈值（美元）',2),('download_link','http://t.cn/RMzEnuJ','APP下载地址',8),('each_transfer_threshold','10000','每笔转账阈值（美元）',3),('enter_maximum_amount','1000000000','最大输入',4),('refund_time','7','转账过期时间（DAY）',7),('total_balance_threshold','10000','总账阈值（美元）',1),('tpps_client_id','b4c9c6c9ae784c3297d6cebf99e2ca47','tpps_client_id',9),('tpps_client_key','fff2226ca87fa0d871278a9b43945b9b','tpps_client_key',10),('tpps_trans_token','dcd8e32afef04989953967deeedae014','tpps_trans_token',11),('verify_time','15','验证码过期时间（MIN）',5);
