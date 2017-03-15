INSERT INTO `e_config` (`config_key`, `config_value`, `config_name`) VALUES ('exchange_fee', '1.5', '兑换手续分（千分比）')


alter table `e_exchange` add column `exchange_fee_per_thousand` decimal(20,4) NOT NULL  COMMENT '手续费千分比' AFTER `exchange_rate`;

alter table `e_exchange` add column `exchange_fee_amount` decimal(20,4) NOT NULL  COMMENT '实际扣除手续费' AFTER `exchange_fee_per_thousand`;