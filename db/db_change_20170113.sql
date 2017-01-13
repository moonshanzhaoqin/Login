ALTER TABLE `anytime_exchange`.`e_config`
  ADD COLUMN `config_canChange` int(1) NOT NULL DEFAULT 0 COMMENT '是否在管理端可更改;0:不可;1:可';