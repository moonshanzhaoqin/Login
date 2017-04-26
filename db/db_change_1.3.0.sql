alter table `e_wallet_seq` change `seq_id` `seq_id` bigint (20)  NOT NULL AUTO_INCREMENT  COMMENT '流水ID';

alter table `e_wallet` add column `update_seq_id` bigint (20) DEFAULT '0' NOT NULL  after `update_time`;

update e_wallet w left join (select MAX(seq_id) as seq_id,user_id,currency from e_wallet_seq group by user_id, currency) s on w.user_id = s.user_id and w.currency = s.currency set w.update_seq_id = s.seq_id;

alter table `e_wallet` add index `index_update_time` (`update_time`);

CREATE TABLE `e_wallet_before` (                                      
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',           
    `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',             
    `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',  
    `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',    
    `update_seq_id` bigint(20) NOT NULL DEFAULT '0',                        
    PRIMARY KEY (`user_id`,`currency`)                                   
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包旧快照'; 
                  
CREATE TABLE `e_wallet_now` (                                      
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',           
    `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',             
    `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',  
    `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',    
    `update_seq_id` bigint(20) NOT NULL DEFAULT '0',                        
    PRIMARY KEY (`user_id`,`currency`)                                   
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包新快照';

CREATE TABLE `e_bad_account` (                                                 
     `bad_account_id` int(11) NOT NULL AUTO_INCREMENT,                            
     `user_id` int(11) NOT NULL DEFAULT '0',                                      
     `currency` char(3) NOT NULL DEFAULT '',                                      
     `sum_amount` decimal(20,4) NOT NULL DEFAULT '0.0000',                        
     `balance_history` decimal(20,4) NOT NULL DEFAULT '0.0000',                   
     `balance_now` decimal(20,4) NOT NULL DEFAULT '0.0000',                       
     `start_time` datetime NOT NULL,                                              
     `end_time` datetime NOT NULL,     
     `start_seq_id` bigint(20) NOT NULL DEFAULT '0',                             
     `end_seq_id` bigint(20) NOT NULL DEFAULT '0',                                               
     `bad_account_status` int(1) NOT NULL DEFAULT '0',                            
     PRIMARY KEY (`bad_account_id`)                                               
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='坏账记录'

alter table `e_wallet_before` add index `index_update_time` (`update_time`);

alter table `e_wallet_before` add index `index_update_seq_id` (`update_seq_id`);

alter table `e_wallet_now` add index `index_update_time` (`update_time`);

alter table `e_wallet_now` add index `index_update_seq_id` (`update_seq_id`);

alter table `e_wallet_seq` add index `index_user_id_currency` (`user_id`, `currency`);

replace into e_wallet_before (user_id,currency,balance,update_time,update_seq_id) 
select user_id,currency,balance,update_time,update_seq_id from e_wallet;

-- 2017/04/06 Niklaus.chi
alter table `e_crm_user_info` add column `create_time` datetime AFTER `user_name`;

-- 2017/04/19 Silent
alter table `e_user` add unique `index_area_phone` (`area_code`, `user_phone`);

-- 2017/04/21 Silent
alter table `e_unregistered` add unique `index_transfer_id` (`transfer_id`);

-- 2017/04/26 Silent
alter table `e_bind` add unique `index_user_id` (`user_id`);
