--2017/12/12
ALTER TABLE `e_user`
  ADD COLUMN `name_pinyin` varchar(255) NULL DEFAULT NULL COMMENT '用户名拼音' AFTER `user_name`;

  --2017/12/13 请开启MySQL的FEDERATED引擎, 并自行修改CONNECTION的链接指向Goldpay的数据库 
CREATE TABLE `g_account` (
  `balance` BIGINT(20) UNSIGNED DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `account_id` VARCHAR(12) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE =FEDERATED CONNECTION='mysql://root:@127.0.0.1:3306/goldq/goldq_account';


--2017/12/14 suzan
ALTER TABLE `e_user`
  ADD COLUMN `user_portrait` varchar(255) NULL DEFAULT NULL COMMENT '头像' AFTER `name_pinyin`;