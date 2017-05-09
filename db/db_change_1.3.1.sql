--2017/05/09 Silent
alter table `e_bad_account` add column `transfer_id` varchar (255) DEFAULT '' NOT NULL  after `bad_account_status`;
  
  