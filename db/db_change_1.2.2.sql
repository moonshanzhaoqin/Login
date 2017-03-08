# Host: 10.1.1.66  (Version 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-03-08 10:01:37
# Generator: MySQL-Front 6.0  (Build 1.11)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "e_withdraw"
#

DROP TABLE IF EXISTS `e_withdraw`;
CREATE TABLE `e_withdraw` (
  `withdraw_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `transfer_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '转账ID',
  `review_status` int(1) NOT NULL DEFAULT '0' COMMENT '审核状态 未审核/通过/未通过',
  `goldpay_remit` int(1) NOT NULL DEFAULT '0' COMMENT 'Goldpay划账结果 未执行/成功/失败',
  PRIMARY KEY (`withdraw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提现审核';
