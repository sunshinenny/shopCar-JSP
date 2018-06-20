/*
 Navicat Premium Data Transfer

 Source Server         : shopCar
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : shopCar

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 20/06/2018 22:15:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `name` varchar(20) NOT NULL DEFAULT '',
  `price` float DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
BEGIN;
INSERT INTO `goods` VALUES ('Apple', 2.8, 550);
INSERT INTO `goods` VALUES ('Banana', 3.1, 139);
INSERT INTO `goods` VALUES ('Orange', 2.3, 102);
INSERT INTO `goods` VALUES ('Pear', 2.5, 149);
INSERT INTO `goods` VALUES ('Tomato', 1.9, -6);
COMMIT;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(20) NOT NULL,
  `date` int(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `price` float(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shopCar
-- ----------------------------
DROP TABLE IF EXISTS `shopCar`;
CREATE TABLE `shopCar` (
  `id` int(10) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `price` float(20,0) DEFAULT NULL,
  `num` int(10) DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `userName` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(20) DEFAULT NULL,
  `sex` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
BEGIN;
INSERT INTO `userinfo` VALUES ('test', '123456', '827957168@qq.com', 'Boy');
INSERT INTO `userinfo` VALUES ('樊海文', 'fanhaiwen', '123456@163.com', 'Boy');
INSERT INTO `userinfo` VALUES ('陶宇豪', '19971118a', '827957168@qq.com', 'Boy');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
