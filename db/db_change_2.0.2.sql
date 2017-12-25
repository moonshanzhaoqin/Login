--2017/12/12
ALTER TABLE `e_user`
  ADD COLUMN `name_pinyin` varchar(255) NULL DEFAULT NULL COMMENT '用户名拼音' AFTER `user_name`;

--2017/12/25
DROP TABLE IF EXISTS `g_account` 
CREATE TABLE `g_account` (
  `balance` BIGINT(20) UNSIGNED DEFAULT NULL,
  `user_id` BIGINT(20) NOT NULL DEFAULT '0',
  `account_id` VARCHAR(12) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='Goldpay冗余表';


--2017/12/14 suzan
ALTER TABLE `e_user`
  ADD COLUMN `user_portrait` varchar(255) NULL DEFAULT NULL COMMENT '头像' AFTER `name_pinyin`;
