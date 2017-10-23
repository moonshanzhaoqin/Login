ALTER TABLE `e_transfer`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `paypal_exchange`;

ALTER TABLE `e_exchange`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `exchange_id`;

DELETE FROM `e_config` WHERE `config_key` = 'tpps_client_id';
DELETE FROM `e_config` WHERE `config_key` = 'tpps_client_key';
DELETE FROM `e_config` WHERE `config_key` = 'tpps_trans_token';

增加系统Goldpay总账号
INSERT INTO `e_bind` (`user_id`, `goldpay_id`, `goldpay_name`, `goldpay_acount`, `token`) VALUES ('1', '1', 'system', '111111111111', ''); 