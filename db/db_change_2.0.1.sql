--2017/11/15 Silent
ALTER TABLE `e_bind` DROP `bind_id`;
ALTER TABLE `e_bind` CHANGE `user_id` `user_id` INT(11) UNSIGNED DEFAULT 0 NOT NULL COMMENT '用户ID', ADD COLUMN `happy_lives_id` VARCHAR(20) DEFAULT '' NULL AFTER `user_id`, ADD PRIMARY KEY (`user_id`);
ALTER TABLE `e_bind` DROP INDEX `index_user_id`;
ALTER TABLE `e_bind` ADD KEY `index_happyLives` (`happy_lives_id`);

ALTER TABLE `e_transfer`
  ADD COLUMN `transfer_fee` decimal(20,4) NULL DEFAULT NULL COMMENT '手续费';
ALTER TABLE `e_trans_detail`
  ADD COLUMN `trans_fee` decimal(20,4) NULL DEFAULT NULL COMMENT '手续费' AFTER `trans_amount`;
ALTER TABLE `e_trans_detail`
  ADD COLUMN `trans_snapshot` varchar(255) NULL DEFAULT NULL COMMENT '交易快照' AFTER `trans_remarks`;

--2017/11/27 suzan
UPDATE `e_crm_admin` SET `admin_power`='1,2,3,4,5,6,7' WHERE `admin_id`=1;

DROP TABLE IF EXISTS `e_withdraw`;
CREATE TABLE `e_withdraw` (
  `withdraw_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_email` varchar(255) NOT NULL DEFAULT '' COMMENT '联系邮箱',
  `quantity` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '金条数量',
  `goldpay` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '对应的goldpay',
  `fee` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '手续费',
  `apply_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `handle_result` tinyint(3) NOT NULL DEFAULT '0' COMMENT '处理结果',
  `handler` varchar(255) DEFAULT NULL COMMENT '处理者',
  `handle_time` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  `gold_transfer_a` varchar(255) DEFAULT NULL COMMENT '金条交易ID(申请)',
  `fee_transfer_a` varchar(255) DEFAULT NULL COMMENT '手续费交易ID(申请)',
  `gold_transfer_b` varchar(255) DEFAULT NULL COMMENT '金条交易ID(失败/成功)',
  `fee_transfer_b` varchar(255) DEFAULT NULL COMMENT '手续费交易ID(失败/成功)',
  PRIMARY KEY (`withdraw_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='提取金条';

DROP TABLE IF EXISTS `e_fee_template`;
CREATE TABLE `e_fee_template` (
  `fee_purpose` varchar(255) NOT NULL DEFAULT '' COMMENT '用途',
  `exempt_amount` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '免手续费额度',
  `fee_percent` decimal(20,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '手续费费率',
  `min_fee` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '最小手续费',
  `max_fee` decimal(20,0) unsigned NOT NULL DEFAULT '999999999' COMMENT '最大手续费',
  `fee_name` varchar(255) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`fee_purpose`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手续费模板';
INSERT INTO `e_fee_template` VALUES ('paypal_purchase_goldbullion_ordinary',100,0.0010,5,100,'Paypal充值（普通）'),('paypal_purchase_goldbullion_vip',0,0.0000,0,999999999,'Paypal充值（VIP）'),('withdraw_goldbullion_ordinary',10000,0.0010,10,999999999,'提取金条（普通）'),('withdraw_goldbullion_vip',0,0.0000,0,999999999,'提取金条（VIP）');
