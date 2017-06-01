--2017/05/24 Suzan
ALTER TABLE `e_crm_user_info`
  ADD COLUMN `login_time` datetime NULL DEFAULT NULL COMMENT '最近登录时间' AFTER `create_time`;
  
--2017/05/25 Silent
insert into `e_config`(`config_key`,`config_value`,`config_name`,`config_order`,`config_canChange`) values ( 'paypal_recharge','true','开启Paypal充值Goldpay','0','1');

--2017/05/25 Suzan
DROP TABLE IF EXISTS `e_crm_log`;
CREATE TABLE `e_crm_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '操作时间',
  `operation` varchar(255) NOT NULL DEFAULT '0' COMMENT '操作',
  `target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员日志';


--2017-05-26 Niklaus
INSERT INTO `e_config` VALUES ('paypal_accessToken', 'access_token$sandbox$h32wtjg3dw3jt4kd$e0a3535f2b04517e66258c0cbe9b118d', 'paypal_accessToken', '0', '0');
INSERT INTO `e_config` VALUES ('paypal_expiration', '600', 'paypal支付过期时间', '0', '0');

ALTER TABLE `e_transfer`
  ADD COLUMN `paypal_exchange` decimal(20,4) NOT NULL  COMMENT 'paypald兑换gp' AFTER `goldpay_acount`;
  ALTER TABLE `e_transfer`
  ADD COLUMN `paypal_currency` char(3) NOT NULL AFTER `goldpay_acount`;