--2017/05/24 Suzan
ALTER TABLE `e_crm_user_info`
  ADD COLUMN `login_time` datetime NULL DEFAULT NULL COMMENT '最近登录时间' AFTER `create_time`;