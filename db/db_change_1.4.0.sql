--2017/05/24 Suzan
ALTER TABLE `e_crm_user_info`
  ADD COLUMN `login_time` datetime NULL DEFAULT NULL COMMENT '最近登录时间' AFTER `create_time`;
  
--2017/05/25 Silent
insert into `e_config`(`config_key`,`config_value`,`config_name`,`config_order`,`config_canChange`) values ( 'paypal_recharge','true','开启Paypal充值Goldpay','0','1');