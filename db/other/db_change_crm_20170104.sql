CREATE TABLE `e_crm_admin` (
	`admin_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
	`admin_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '管理员登录名',
	`admin_password` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '管理员登录密码',
	`password_salt` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '盐值',
	PRIMARY KEY (`admin_id`)
)
COMMENT='管理员'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=7
;

CREATE TABLE `e_crm_alarm` (
	`alarm_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`alarm_type` INT(1) NULL DEFAULT '0' COMMENT '报警类型，暂无其他均为0',
	`alarm_grade` VARCHAR(255) NULL DEFAULT NULL COMMENT '报警等级',
	`lower_limit` DECIMAL(10,0) NULL DEFAULT NULL COMMENT '下限',
	`upper_limit` DECIMAL(10,0) NULL DEFAULT NULL COMMENT '上限',
	`alarm_mode` INT(11) NULL DEFAULT '1' COMMENT '报警方式 1：短信2：邮件3短信+邮件',
	`supervisor_id_arr` VARCHAR(255) NULL DEFAULT NULL COMMENT '存储相关人员的id',
	`create_at` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`editor_id` INT(11) NULL DEFAULT NULL COMMENT '编辑人id',
	PRIMARY KEY (`alarm_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10
;

CREATE TABLE `e_crm_supervisor` (
	`supervisor_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`supervisor_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '监督人姓名',
	`supervisor_mobile` VARCHAR(255) NULL DEFAULT NULL COMMENT '手机号',
	`supervisor_email` VARCHAR(255) NULL DEFAULT NULL,
	`update_at` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`supervisor_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;

CREATE TABLE `e_crm_user_info` (
	`user_id` BIGINT(20) NOT NULL,
	`area_code` VARCHAR(10) NULL DEFAULT NULL COMMENT '区号',
	`user_phone` VARCHAR(30) NULL DEFAULT NULL COMMENT '手机号',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户名',
	`user_type` INT(1) NULL DEFAULT NULL COMMENT '用户类型',
	`user_available` INT(1) NULL DEFAULT NULL COMMENT '用户是否冻结',
	`user_total_assets` DECIMAL(10,0) NULL DEFAULT NULL COMMENT '用户总资产',
	`update_at` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`user_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

ALTER TABLE `e_crm_alarm`
	ADD COLUMN `alarm_available` int(1)COMMENT '报警可用，0false1true' AFTER `supervisor_id_arr`;




