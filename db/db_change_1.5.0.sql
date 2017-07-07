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