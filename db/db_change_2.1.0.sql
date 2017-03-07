# Host: 10.1.1.66  (Version 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-03-07 10:11:12
# Generator: MySQL-Front 6.0  (Build 1.11)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "e_withdraw_review"
#

DROP TABLE IF EXISTS `e_withdraw_review`;
CREATE TABLE `e_withdraw_review` (
  `withdraw_id` int(11) NOT NULL AUTO_INCREMENT,
  `transfer_id` varchar(255) NOT NULL DEFAULT '0' COMMENT '转账ID',
  `review_status` int(1) NOT NULL DEFAULT '0' COMMENT '审核状态',
  PRIMARY KEY (`withdraw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提现审核';
