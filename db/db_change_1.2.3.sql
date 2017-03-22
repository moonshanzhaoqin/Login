alter table `e_wallet` add column `update_seq_id` int (11) DEFAULT '0' NOT NULL  after `update_time`;

update e_wallet w left join (select MAX(seq_id) as seq_id,user_id,currency from e_wallet_seq group by user_id, currency) s on w.user_id = s.user_id and w.currency = s.currency set w.update_seq_id = s.seq_id;

alter table `e_wallet` add index `index_update_time` (`update_time`);

CREATE TABLE `e_wallet_history` (                                      
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',           
    `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',             
    `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',  
    `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',    
    `update_seq_id` int(11) NOT NULL DEFAULT '0',                        
    PRIMARY KEY (`user_id`,`currency`)                                   
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包' 
                  
CREATE TABLE `e_wallet_now` (                                      
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',           
    `currency` char(3) NOT NULL DEFAULT '' COMMENT '币种',             
    `balance` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',  
    `update_time` datetime DEFAULT NULL COMMENT '最新变动时间',    
    `update_seq_id` int(11) NOT NULL DEFAULT '0',                        
    PRIMARY KEY (`user_id`,`currency`)                                   
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包' 

alter table `e_wallet_history` add index `index_update_time` (`update_time`);

alter table `e_wallet_history` add index `index_update_seq_id` (`update_seq_id`);

alter table `e_wallet_now` add index `index_update_time` (`update_time`);

alter table `e_wallet_now` add index `index_update_seq_id` (`update_seq_id`);