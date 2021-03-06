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
  
ALTER TABLE e_transfer MODIFY paypal_currency CHAR(3) DEFAULT '';
ALTER TABLE e_transfer MODIFY paypal_exchange DECIMAL(20,4) DEFAULT 0;


--2017-06-05 Suzan
ALTER TABLE `e_crm_admin`
  ADD COLUMN `admin_power` varchar(255) NULL DEFAULT NULL COMMENT '权限';
  
  
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) 
values('total_gdq_can_be_sold','100000000','可售沛金条总量','0','1'); 
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) 
values('paypal_max_limit_each_time','100000000','paypal单笔交易金额上限','0','1');  
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) 
values('paypal_mini_limit_each_time','100','paypal单笔交易金额下限','0','1');

alter table `e_wallet` drop key `userid_currency`, add unique `userid_currency` (`user_id`, `currency`);
