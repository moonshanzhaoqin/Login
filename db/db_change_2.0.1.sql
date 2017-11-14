--2017/11/14  suzan.wu
DROP TABLE IF EXISTS `e_fee_template`;
CREATE TABLE `e_fee_template` (
  `fee_purpose` varchar(255) NOT NULL DEFAULT '' COMMENT '用途',
  `exempt_amount` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '免手续费额度',
  `fee_percent` decimal(20,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '手续费费率',
  `min_fee` decimal(20,0) unsigned NOT NULL DEFAULT '0' COMMENT '最小手续费',
  `max_fee` decimal(20,0) unsigned NOT NULL DEFAULT '999999999' COMMENT '最大手续费',
  PRIMARY KEY (`fee_purpose`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手续费模板'; 