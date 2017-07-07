DROP TABLE IF EXISTS `e_trans_details`;
CREATE TABLE `e_trans_details` (
  `details_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `transfer_id` varchar(100) NOT NULL COMMENT 'transferId',
  `user_id` int(11) DEFAULT NULL COMMENT '当前用户的Id',
  `trader_name` varchar(100) DEFAULT NULL COMMENT '交易人姓名',
  `trader_area_code` varchar(30) DEFAULT NULL COMMENT '交易人手机区号',
  `trader_phone` varchar(30) DEFAULT NULL COMMENT '交易人手机号码',
  `trans_currency` char(3) DEFAULT NULL,
  `trans_amount` decimal(20,4) DEFAULT NULL,
  `trans_remarks` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`details_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42160 DEFAULT CHARSET=utf8;