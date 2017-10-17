
-- add by niklaus.chi at 2017-10-16
INSERT INTO `e_config` (`config_key`, `config_value`, `config_name`) VALUES ('goldpay_system_account', 'ssss', 'Goldpay系统账户')

ALTER TABLE `e_transfer`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `paypal_exchange`

ALTER TABLE `e_exchange`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `exchange_id`