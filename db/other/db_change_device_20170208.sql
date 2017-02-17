# Host: 10.1.1.66  (Version 5.6.19-0ubuntu0.14.04.1)
# Date: 2017-02-08 17:39:10
# Generator: MySQL-Front 6.0  (Build 1.11)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "e_user_device"
#

DROP TABLE IF EXISTS `e_user_device`;
CREATE TABLE `e_user_device` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `device_id` varchar(255) NOT NULL DEFAULT '' COMMENT '设备ID',
  `device_name` varchar(255) NOT NULL DEFAULT '' COMMENT '设备名',
  PRIMARY KEY (`user_id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设备';
