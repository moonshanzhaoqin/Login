#2017-02-17 数据库乐观锁
alter table `e_exchange` add column `version` int (11) DEFAULT '0' NOT NULL  COMMENT 'version';
alter table `e_transfer` add column `version` int (11) DEFAULT '0' NOT NULL  COMMENT 'version';