/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50132
Source Host           : localhost:3306
Source Database       : nutrition

Target Server Type    : MYSQL
Target Server Version : 50132
File Encoding         : 65001

Date: 2015-08-13 20:39:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for collection_table
-- ----------------------------
DROP TABLE IF EXISTS `collection_table`;
CREATE TABLE `collection_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collectionTime` date DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `publishId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA7805EEDAF45F9B2` (`publishId`),
  KEY `FKA7805EED78723658` (`userId`),
  CONSTRAINT `FKA7805EED78723658` FOREIGN KEY (`userId`) REFERENCES `user_table` (`id`),
  CONSTRAINT `FKA7805EEDAF45F9B2` FOREIGN KEY (`publishId`) REFERENCES `publish_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for food_table
-- ----------------------------
DROP TABLE IF EXISTS `food_table`;
CREATE TABLE `food_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `foodName` varchar(20) DEFAULT NULL,
  `foodWeight` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for food_table_goods
-- ----------------------------
DROP TABLE IF EXISTS `food_table_goods`;
CREATE TABLE `food_table_goods` (
  `foodId` bigint(20) NOT NULL,
  `goodId` bigint(20) NOT NULL,
  PRIMARY KEY (`foodId`,`goodId`),
  KEY `FKEEFE714453E5DE67` (`goodId`),
  KEY `FKEEFE71445E9E9EBE` (`foodId`),
  CONSTRAINT `FKEEFE714453E5DE67` FOREIGN KEY (`goodId`) REFERENCES `goods_table` (`id`),
  CONSTRAINT `FKEEFE71445E9E9EBE` FOREIGN KEY (`foodId`) REFERENCES `food_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for goods_table
-- ----------------------------
DROP TABLE IF EXISTS `goods_table`;
CREATE TABLE `goods_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goodsName` varchar(20) DEFAULT NULL,
  `nutritionalEffects` varchar(200) DEFAULT NULL,
  `suitablPerson` varchar(20) DEFAULT NULL,
  `step` varchar(200) DEFAULT NULL,
  `publishTime` date DEFAULT NULL,
  `goodsPhoto` varchar(50) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `goodsPhoto` (`goodsPhoto`),
  KEY `FK1D96DDA578723658` (`userId`),
  CONSTRAINT `FK1D96DDA578723658` FOREIGN KEY (`userId`) REFERENCES `user_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for goods_table_foods
-- ----------------------------
DROP TABLE IF EXISTS `goods_table_foods`;
CREATE TABLE `goods_table_foods` (
  `goodsId` bigint(20) NOT NULL,
  `foodId` bigint(20) NOT NULL,
  PRIMARY KEY (`goodsId`,`foodId`),
  KEY `FK968F30BBAA29B2C0` (`goodsId`),
  KEY `FK968F30BB5E9E9EBE` (`foodId`),
  CONSTRAINT `FK968F30BB5E9E9EBE` FOREIGN KEY (`foodId`) REFERENCES `food_table` (`id`),
  CONSTRAINT `FK968F30BBAA29B2C0` FOREIGN KEY (`goodsId`) REFERENCES `goods_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for goods_table_types
-- ----------------------------
DROP TABLE IF EXISTS `goods_table_types`;
CREATE TABLE `goods_table_types` (
  `goodsId` bigint(20) NOT NULL,
  `typeId` bigint(20) NOT NULL,
  PRIMARY KEY (`goodsId`,`typeId`),
  KEY `FK9759095F77165D76` (`typeId`),
  KEY `FK9759095FAA29B2C0` (`goodsId`),
  CONSTRAINT `FK9759095F77165D76` FOREIGN KEY (`typeId`) REFERENCES `type_table` (`id`),
  CONSTRAINT `FK9759095FAA29B2C0` FOREIGN KEY (`goodsId`) REFERENCES `goods_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for publish_table
-- ----------------------------
DROP TABLE IF EXISTS `publish_table`;
CREATE TABLE `publish_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `publishContent` varchar(100) DEFAULT NULL,
  `publishPicture` varchar(50) DEFAULT NULL,
  `publishTime` date DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `publishPicture` (`publishPicture`),
  KEY `FK8C78F49E78723658` (`userId`),
  CONSTRAINT `FK8C78F49E78723658` FOREIGN KEY (`userId`) REFERENCES `user_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for question_table
-- ----------------------------
DROP TABLE IF EXISTS `question_table`;
CREATE TABLE `question_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baseAnswe` varchar(30) DEFAULT NULL,
  `anserDetail` varchar(100) DEFAULT NULL,
  `statu` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `baseAnswe` (`baseAnswe`),
  UNIQUE KEY `anserDetail` (`anserDetail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for type_table
-- ----------------------------
DROP TABLE IF EXISTS `type_table`;
CREATE TABLE `type_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `typeName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `typeName` (`typeName`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for type_table_goods
-- ----------------------------
DROP TABLE IF EXISTS `type_table_goods`;
CREATE TABLE `type_table_goods` (
  `typeId` bigint(20) NOT NULL,
  `goodsId` bigint(20) NOT NULL,
  PRIMARY KEY (`typeId`,`goodsId`),
  KEY `FK97EB8A2077165D76` (`typeId`),
  KEY `FK97EB8A20AA29B2C0` (`goodsId`),
  CONSTRAINT `FK97EB8A2077165D76` FOREIGN KEY (`typeId`) REFERENCES `type_table` (`id`),
  CONSTRAINT `FK97EB8A20AA29B2C0` FOREIGN KEY (`goodsId`) REFERENCES `goods_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_table
-- ----------------------------
DROP TABLE IF EXISTS `user_table`;
CREATE TABLE `user_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `userPhoto` varchar(50) DEFAULT NULL,
  `questionStatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userPhoto` (`userPhoto`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
