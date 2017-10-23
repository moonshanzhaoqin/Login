ALTER TABLE `e_transfer`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `paypal_exchange`

ALTER TABLE `e_exchange`     
ADD COLUMN `goldpay_order_id` varchar(255) COMMENT 'goldpayOrderId' AFTER `exchange_id`