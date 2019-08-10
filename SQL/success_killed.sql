/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : spring

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-08-10 09:17:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for success_killed
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `seckill_id` int(11) NOT NULL COMMENT '秒杀商品id',
  `user_phone` varchar(255) NOT NULL,
  `state` tinyint(4) DEFAULT '-1' COMMENT '-1：无效，0：成功，1：已付款',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
