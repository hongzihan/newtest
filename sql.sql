-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_item`;
CREATE TABLE `tb_item` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `keyname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_item` VALUES ('1', '金箍棒', '小小白', '1');
INSERT INTO `tb_item` VALUES ('2', '无敌金箍棒', '大大白', '2');





-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `count_item`;
CREATE TABLE `count_item` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `keyname` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `count_item` VALUES (0, '羊哒迪奥', '1');
INSERT INTO `count_item` VALUES (0, '无敌羊哒迪奥', '2');


DROP TABLE IF EXISTS `drop_item`;
CREATE TABLE `drop_item` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `keyname` varchar(255) NOT NULL,
  `itemname` varchar(255) NOT NULL,
  `count` int(11) NOT NULL,
  `zoneid` varchar(63) not null,
  `dateTime` varchar(63) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

INSERT INTO `drop_item` VALUES (0, '羊哒迪奥', "杨大吊", '1');

DROP TABLE IF EXISTS `crud_st_item`;
CREATE TABLE `crud_st_item` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `zoneid` varchar(255) DEFAULT NULL,
  `playername` varchar(255) DEFAULT NULL,
  `keyname` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

INSERT INTO `crud_st_item` VALUES (0, '530868', "小小白", '积分券10', 1, 1);

DROP TABLE IF EXISTS `web_action`;
CREATE TABLE `web_action` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `zoneid` varchar(255) NOT NULL,
  `actiontype` int(11) NOT NULL,
  `actiondata` varchar(1023) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `charge`;
CREATE TABLE `charge` (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `username` varchar(63) NOT NULL,
  `accountName` varchar(63) NOT NULL,
  `zoneName` varchar(63) NOT NULL,
  `chargeNum` int(15) NOT NULL,
  `dateTime` varchar(63) NOT NULL,
  `chargeCount` int(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  unique key AK_nq_username (username)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;
INSERT INTO `user` VALUES (0, 'root', "123456", '1601790617@qq.com', 199807);

-- 权限五表
DROP TABLE IF EXISTS `user`;
create table user(
 `id` bigint(25) NOT NULL PRIMARY KEY AUTO_INCREMENT,
 `username` VARCHAR(20) NOT NULL ,
 `password` VARCHAR(127) NOT NULL ,
 `nickname` VARCHAR(20) NOT NULL,
 `corepwd` int(11) NOT NULL,
 unique key AK_nq_username (username)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

-- alter table `user` change password password varchar(127) not null;

DROP TABLE IF EXISTS `role`;
create table role(
id bigint(25) NOT NULL PRIMARY KEY AUTO_INCREMENT,
rolename VARCHAR(127) NOT NULL,
roledesc VARCHAR(127),
unique key AK_nq_rolename (rolename)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

-- alter table `role` change rolename rolename varchar(127) not null;
-- alter table `role` change roledesc roledesc varchar(127) not null;

DROP TABLE IF EXISTS `permission`;
create table permission(
id bigint(25) NOT NULL PRIMARY KEY AUTO_INCREMENT,
modelname VARCHAR(255) NOT NULL ,
permission VARCHAR(127) NOT NULL,
unique key AK_nq_modelname (modelname)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user_role`;
create table user_role(
uid bigint(25) NOT NULL ,
rid bigint(25) NOT NULL ,
PRIMARY KEY (`uid`, `rid`),
CONSTRAINT `fk-uid-ur` FOREIGN KEY (`uid`) REFERENCES `user` (`id`),
CONSTRAINT `fk-rid-ur` FOREIGN KEY (`rid`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role_permission`;
create table role_permission(
rid bigint(25) NOT NULL,
pid bigint(25) NOT NULL,
PRIMARY KEY (`rid`, `pid`),
CONSTRAINT `fk-rid-rp` FOREIGN KEY (`rid`) REFERENCES `role` (`id`),
CONSTRAINT `fk-pid-rp` FOREIGN KEY (`pid`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `chat_record`;
create table chat_record(
id bigint(25) NOT NULL PRIMARY KEY AUTO_INCREMENT,
zone_name VARCHAR(63) NOT NULL,
date_time VARCHAR(63) NOT NULL,
content VARCHAR(63) NOT NULL,
username VARCHAR(63) NOT NULL,
channel_name VARCHAR(63) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `config`;
create table config(
id bigint(25) NOT NULL PRIMARY KEY AUTO_INCREMENT,
config_name VARCHAR(63) NOT NULL,
config_value VARCHAR(4095) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8;


-- alter table permission change modelname modelname varchar(200) not null;
-- alter table permission change permission permission varchar(100) not null;
INSERT INTO `user` VALUES (2, 'root', "123456", '张三', 199807);
INSERT INTO `role` VALUES (2, 'super manager', 'max permission');
INSERT INTO `permission` VALUES (2, '/index', '首页');
INSERT INTO `user_role` VALUES (2, 2, 2);
INSERT INTO `role_permission` VALUES (2, 2, 2);

INSERT INTO `permission` VALUES (3, '/saveActionData_item', '保存物品操作');
INSERT INTO `role_permission` VALUES (3, 2, 3);

INSERT INTO `permission` VALUES (4, '/saveActionData_item', 'index');
INSERT INTO `role_permission` VALUES (4, 2, 4);

INSERT INTO `permission` VALUES (5, '/saveActionData_item', 'form-common');
INSERT INTO `role_permission` VALUES (5, 2, 5);

INSERT INTO `permission` VALUES (0, '/webAction/saveActionData_item', 'saveActionData_item');
INSERT INTO `permission` VALUES (0, '/webAction/saveActionData_mail', 'saveActionData_mail');
INSERT INTO `permission` VALUES (0, '/webAction/saveActionData_var', 'saveActionData_var');

INSERT INTO `role_permission` values (1, 2);
INSERT INTO `role_permission` values (1, 3);
INSERT INTO `role_permission` values (1, 4);
INSERT INTO `role_permission` values (1, 5);
INSERT INTO `role_permission` values (1, 6);
INSERT INTO `role_permission` values (1, 7);
INSERT INTO `role_permission` values (2, 1);
INSERT INTO `role_permission` values (2, 2);

select p1.*,p2.*,p3.* from user p1, role p2, permission p3;


select p1.*,p2.*,p3.* from user p1
INNER JOIN user_role ur on p1.id = ur.uid
INNER JOIN role p2 on p2.id = ur.rid
INNER JOIN role_permission rp on p2.id = rp.rid
INNER JOIN permission p3 on p3.id = rp.pid
where p1.username = 'root';

delete p1,p2,p3 from user p1
INNER JOIN user_role ur on p1.id = ur.uid
INNER JOIN role p2 on p2.id = ur.rid
INNER JOIN role_permission rp on p2.id = rp.rid
INNER JOIN permission p3 on p3.id = rp.pid
where p1.username = 'root';

-- INSERT INTO p1,p2,p3,ur,rp from user p1
-- INNER JOIN user_role ur on p1.id = ur.uid
-- INNER JOIN role p2 on p2.id = ur.rid
-- INNER JOIN role_permission rp on p2.id = rp.rid
-- INNER JOIN permission p3 on p3.id = rp.pid
-- VALUES (2, 'root', "123456", '张三', 199807,
-- 2, 'super manager', 'max permission',
-- 2, '/index', '首页',
-- 2, 2, 2,
-- 2, 2, 2)

-- 为monster_die表添加自动删除2天前的数据
-- 创建存储过程
DELIMITER //
CREATE PROCEDURE autodel()
     BEGIN
		 delete From monster_die where DATE(dietime) <= DATE(DATE_SUB(NOW(),INTERVAL 2 day));
		 delete From drop_item where DATE(dateTime) <= DATE(DATE_SUB(NOW(),INTERVAL 10 day));
         delete From drop_item where DATE(dateTime) <= DATE(DATE_SUB(NOW(),INTERVAL 5 day)) AND count > 1;
         delete From chat_record where DATE(date_time) <= DATE(DATE_SUB(NOW(),INTERVAL 2 day));
     END
     //
DELIMITER ;
-- 创建事件
CREATE EVENT `event_auto_del_memorydata`
ON SCHEDULE EVERY 1 DAY STARTS '2020-09-03 16:00:00'
ON COMPLETION NOT PRESERVE ENABLE DO CALL autodel();

--开启事件计划（调度器）4种方法。键值1或者ON表示开启；0或者OFF表示关闭：
SET GLOBAL event_scheduler = 1;
SET @@global.event_scheduler = 1;
SET GLOBAL event_scheduler = ON;
SET @@global.event_scheduler = ON;

--查看当前是否已开启事件计划（调度器）3种方法：
SHOW VARIABLES LIKE 'event_scheduler';
SELECT @@event_scheduler;
SHOW PROCESSLIST;

--事件开启与关闭：
ALTER EVENT event_auto_del_memorydata ON COMPLETION PRESERVE ENABLE; --开启某事件
ALTER EVENT event_auto_del_memorydata ON COMPLETION PRESERVE DISABLE; --关闭某事件

--删除存储过程：
DROP PROCEDURE pro_clear_data;
DROP PROCEDURE autodel;

--删除Event:
DROP EVENT IF EXISTS event_time_clear_data1
DROP EVENT IF EXISTS event_auto_del_memorydata