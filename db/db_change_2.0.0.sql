ALTER TABLE `e_transfer`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `paypal_exchange`;

ALTER TABLE `e_exchange`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `exchange_id`;

DELETE FROM `e_config` WHERE `config_key` = 'tpps_client_id';
DELETE FROM `e_config` WHERE `config_key` = 'tpps_client_key';
DELETE FROM `e_config` WHERE `config_key` = 'tpps_trans_token';

增加系统Goldpay总账号
INSERT INTO `e_bind` (`user_id`, `goldpay_id`, `goldpay_name`, `goldpay_acount`, `token`) VALUES ('1', '1', 'system', '111111111111', ''); 


--2017/10/23 suzan
INSERT INTO `e_config` SET `config_key`='bank_recharge',`config_value`='true',`config_name`='开启银行汇款充值Goldpay',`config_order`=0,`config_canChange`=0;
DELETE FROM `e_config` WHERE `config_key`='goldpay_withdraw';

--2017/10/31 silent
ALTER TABLE `e_user` ADD COLUMN `user_pay_token` VARCHAR(128) CHARSET utf8 COLLATE utf8_general_ci DEFAULT '' NULL COMMENT '支付自动转账token' AFTER `user_pay_pwd`; 

--2017/11/01 suzan
ALTER TABLE `e_collect`
  CHANGE COLUMN `user_phone` `user_phone` varchar(11) NOT NULL DEFAULT '' COMMENT '用户手机号';
ALTER TABLE `e_collect`
  ADD UNIQUE INDEX `area_phone_time` (`area_code`(5),`user_phone`(11),`collect_time`),
  DROP INDEX `area_phone_time`;