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

INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_from,t3.user_name,
t2.area_code,t2.phone,
t2.currency,-t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_to = t3.user_id and t2.transfer_type = 0;


INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_to,t3.user_name,
t3.area_code,t3.user_phone,
t2.currency,t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_from = t3.user_id and t2.transfer_type = 0;

INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_from,t3.user_name,
t2.area_code,t2.phone,
t2.currency,-t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_to = t3.user_id and t2.transfer_type = 2;

INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_to,t3.user_name,
t2.area_code,t2.phone,
t2.currency,t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_from = t3.user_id and t2.transfer_type = 3;

INSERT INTO 
e_trans_details(transfer_id,user_id,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_from,
t2.currency,-t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2 WHERE
t2.transfer_type = 4;

INSERT INTO 
e_trans_details(transfer_id,user_id,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_to,
t2.currency,t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2 WHERE
t2.transfer_type = 5;

INSERT INTO 
e_trans_details(transfer_id,user_id,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_to,
t2.currency,t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2 WHERE
t2.transfer_type = 7;