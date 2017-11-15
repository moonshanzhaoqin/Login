ALTER TABLE `e_unregistered`
	ADD COLUMN `refund_trans_id` VARCHAR(255) NULL COMMENT '退款时交易号' AFTER `transfer_id`;