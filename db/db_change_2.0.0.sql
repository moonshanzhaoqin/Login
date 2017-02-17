#2017-02-17 数据库乐观锁
alter table `e_exchange` add column `version` int (11) DEFAULT '0' NOT NULL  COMMENT 'version';
alter table `e_transfer` add column `version` int (11) DEFAULT '0' NOT NULL  COMMENT 'version';

ALTER TABLE `e_user`     
ADD COLUMN `login_available` INT(1) DEFAULT '1' NOT NULL COMMENT '登录冻结标识 0:冻结 1:正常' AFTER `user_available`,     
ADD COLUMN `pay_available` INT(1) DEFAULT '1' NOT NULL COMMENT '支付冻结标识 0:冻结 1:正常' AFTER `login_available`;

insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('exchange_limit_daily_pay','3000','每天兑换金额限制','21','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('exchange_limit_number_of_pay_per_day','10','每天兑换次数限制','22','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('exchange_limit_per_pay','300','每次兑换金额限制','20','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('login_unavailiable_time','3','登录冻结时间（H）','15','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('pay_unavailiable_time','3','支付冻结时间（H）','13','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('transfer_limit_daily_pay','3000','每天支付金额限制','18','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('transfer_limit_number_of_pay_per_day','10','每天支付次数限制','19','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('transfer_limit_per_pay','100','每次支付金额限制','17','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('wrong_password_frequency','10','错误登录密码次数','16','0');
insert into `e_config` (`config_key`, `config_value`, `config_name`, `config_order`, `config_canChange`) values('wrong_paypwd_frequency','3','错误支付密码次数','14','0');
