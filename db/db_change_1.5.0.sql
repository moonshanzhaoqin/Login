--2017/07/05 Suzan
INSERT INTO `e_config` SET `config_key`='goldpay_withdraw',`config_value`='true',`config_name`='提取沛金条开关',`config_order`=0,`config_canChange`=0;


--2017/07/07 Suzan
DROP TABLE IF EXISTS `e_campaign`;
CREATE TABLE `e_campaign` (
  `campaign_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `start_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '开始时间',
  `end_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `campaign_status` int(1) NOT NULL DEFAULT '0' COMMENT '活动状态',
  `campaign_budget` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '活动预算',
  `budget_surplus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '预算剩余',
  `inviter_bonus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '邀请人奖励金',
  `invitee_bonus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '被邀请人奖励金',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最新更新时间',
  PRIMARY KEY (`campaign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推广活动';

DROP TABLE IF EXISTS `e_collect`;
CREATE TABLE `e_collect` (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `inviter_id` int(11) NOT NULL DEFAULT '0' COMMENT '邀请人ID',
  `campaign_id` int(11) NOT NULL DEFAULT '0' COMMENT '活动ID',
  `inviter_bonus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '邀请人奖励金',
  `invitee_bonus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '被邀请人奖励金',
  `collect_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '领取时间',
  `register_status` int(1) NOT NULL DEFAULT '0' COMMENT '注册状态',
  `share_path` int(1) NOT NULL DEFAULT '0' COMMENT '分享途径',
  PRIMARY KEY (`collect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='领取';

--2017/07/10 Niklaus Chi 
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
t3.area_code,t3.user_phone,
t2.currency,-t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_to = t3.user_id and t2.transfer_type = 0 and t2.user_from != 1;


INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks)
SELECT 
t2.transfer_id,t2.user_to,t3.user_name,
t3.area_code,t3.user_phone,
t2.currency,t2.transfer_amount,
t2.transfer_comment 
FROM e_transfer t2,e_user t3 WHERE
t2.user_from = t3.user_id and t2.transfer_type = 0 and t2.user_from != 1;

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

ALTER TABLE `e_trans_details`     
ADD COLUMN `details_create_time` datetime COMMENT 'details创建时间' AFTER `trans_remarks`,

--2017/07/10 Suzan
INSERT INTO `e_config` SET `config_key`='collect_active_time',`config_value`='2',`config_name`='领取有效时间（H）',`config_order`=0,`config_canChange`=1;
INSERT INTO `e_config` SET `config_key`='invite_quantity_restriction',`config_value`='10',`config_name`='邀请数量限制',`config_order`=0,`config_canChange`=1;

DROP TABLE IF EXISTS `e_inviter`;
CREATE TABLE `e_inviter` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `invite_quantity` int(5) NOT NULL DEFAULT '0' COMMENT '邀请人数',
  `invite_bonus` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '邀请奖励金',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邀请人';


--add by nicholas.chi at 2017/07/14
INSERT INTO 
e_trans_details(transfer_id,user_id,trader_name,trader_area_code,trader_phone,trans_currency,trans_amount,trans_remarks) 
SELECT 
t2.transfer_id,t2.user_to,t4.user_name,t4.area_code,t4.user_phone,t2.currency,t2.transfer_amount,t3.transfer_comment 
FROM e_unregistered t1 
LEFT JOIN e_transfer t2 ON t1.transfer_id = t2.transfer_comment 
LEFT JOIN e_transfer t3 ON t1.transfer_id = t3.transfer_id 
LEFT JOIN e_user t4 ON t3.user_from = t4.user_id  
WHERE unregistered_status = 2;