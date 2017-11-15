# Host: 10.1.1.66  (Version 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-02-16 10:58:16
# Generator: MySQL-Front 6.0  (Build 1.11)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "e_user"
#

CREATE TABLE `e_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `area_code` varchar(5) NOT NULL DEFAULT '' COMMENT '国家码',
  `user_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `user_name` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_pay_pwd` varchar(255) DEFAULT NULL COMMENT '用户支付密码',
  `create_time` datetime NOT NULL COMMENT '用户注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `login_ip` varchar(255) DEFAULT NULL COMMENT '最近登录IP',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型(0:普通 1:系统)',
  `user_available` int(1) NOT NULL DEFAULT '1' COMMENT '账户冻结标识 0:冻结 1:正常',
  `login_available` int(1) NOT NULL DEFAULT '1' COMMENT '登录冻结标识 0:冻结 1:正常',
  `pay_available` int(1) NOT NULL DEFAULT '1' COMMENT '支付冻结标识 0:冻结 1:正常',
  `password_salt` varchar(255) NOT NULL DEFAULT '' COMMENT '盐值(用户密码,支付密码)',
  `push_id` varchar(255) DEFAULT NULL COMMENT '设备号',
  `push_tag` int(2) NOT NULL DEFAULT '0' COMMENT '语言',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='用户';
