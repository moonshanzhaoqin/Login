alter table `e_wallet` add column `update_seq_id` int (11) DEFAULT '0' NOT NULL  after `update_time`;

update e_wallet w left join (select MAX(seq_id) as seq_id,user_id,currency from e_wallet_seq group by user_id, currency) s on w.user_id = s.user_id and w.currency = s.currency set w.update_seq_id = s.seq_id;


