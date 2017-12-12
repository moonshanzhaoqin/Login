--2017/12/12
ALTER TABLE `e_user`
  ADD COLUMN `name_pinyin` varchar(255) NULL DEFAULT NULL COMMENT '用户名拼音';