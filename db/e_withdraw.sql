# Host: 10.1.1.66  (Version 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-11-28 18:32:33
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "e_withdraw"
#

DROP TABLE IF EXISTS `e_withdraw`;
CREATE TABLE `e_withdraw` (
  `withdraw_id` varchar(255) NOT NULL DEFAULT '' COMMENT '提取申请编号',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_email` varchar(255) NOT NULL DEFAULT '' COMMENT '联系邮箱',
  `quantity` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '金条数量',
  `goldpay` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '对应的goldpay',
  `fee` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '手续费',
  `apply_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `handle_result` tinyint(3) NOT NULL DEFAULT '0' COMMENT '提取结果',
  `handler` varchar(255) DEFAULT NULL COMMENT '处理者',
  `handle_time` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  `gold_transfer_a` varchar(255) DEFAULT NULL COMMENT '金条交易ID(申请)',
  `fee_transfer_a` varchar(255) DEFAULT NULL COMMENT '手续费交易ID(申请)',
  `gold_transfer_b` varchar(255) DEFAULT NULL COMMENT '金条交易ID(失败/成功)',
  `fee_transfer_b` varchar(255) DEFAULT NULL COMMENT '手续费交易ID(失败/成功)',
  PRIMARY KEY (`withdraw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提取金条';
