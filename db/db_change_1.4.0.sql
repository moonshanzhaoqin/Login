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