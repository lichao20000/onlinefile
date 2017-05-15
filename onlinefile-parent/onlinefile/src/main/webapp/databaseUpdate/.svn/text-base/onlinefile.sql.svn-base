/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : onlinefile

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2015-02-25 11:14:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chatrecords_0
-- ----------------------------
DROP TABLE IF EXISTS `chatrecords_0`;
CREATE TABLE `chatrecords_0` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `GROUPID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  `TIME` varchar(30) DEFAULT NULL,
  `CONTEXT` varchar(4000) DEFAULT NULL,
  `FILEID` int(8) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `GROUPID` (`GROUPID`),
  KEY `USERID` (`USERID`),
  KEY `FILEID` (`FILEID`),
  CONSTRAINT `chatrecords_0_ibfk_1` FOREIGN KEY (`GROUPID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chatrecords_0_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chatrecords_0_ibfk_3` FOREIGN KEY (`FILEID`) REFERENCES `files_0` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chatrecords_0
-- ----------------------------

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  `ADDRESSES` varchar(500) DEFAULT NULL,
  `PHONE` varchar(13) DEFAULT NULL,
  `FAX` varchar(13) DEFAULT NULL,
  `POSTCODE` varchar(20) DEFAULT NULL,
  `URL` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('0', '东方飞扬软件股份有限公司', null, null, null, null, '');

-- ----------------------------
-- Table structure for companymanager
-- ----------------------------
DROP TABLE IF EXISTS `companymanager`;
CREATE TABLE `companymanager` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `COMPANYID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `companymanager_ibfk_1` (`COMPANYID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `companymanager_ibfk_1` FOREIGN KEY (`COMPANYID`) REFERENCES `company` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `companymanager_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of companymanager
-- ----------------------------

-- ----------------------------
-- Table structure for documentclass
-- ----------------------------
DROP TABLE IF EXISTS `documentclass`;
CREATE TABLE `documentclass` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `COMPANYID` int(8) NOT NULL,
  `CLASSNAME` varchar(50) NOT NULL,
  `ORDER` int(8) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPANYID` (`COMPANYID`),
  CONSTRAINT `documentclass_ibfk_1` FOREIGN KEY (`COMPANYID`) REFERENCES `company` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of documentclass
-- ----------------------------

-- ----------------------------
-- Table structure for emailset
-- ----------------------------
DROP TABLE IF EXISTS `emailset`;
CREATE TABLE `emailset` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `USERID` int(8) NOT NULL,
  `TYPE` varchar(30) DEFAULT NULL,
  `RECEIVESERVER` varchar(100) DEFAULT NULL,
  `SENDSERVER` varchar(100) DEFAULT NULL,
  `RECEIVESERVERPORT` varchar(100) DEFAULT NULL,
  `SENDSERVERPORT` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `PASSWORD` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `emailset_ibfk_1` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emailset
-- ----------------------------

-- ----------------------------
-- Table structure for ess_appconfig
-- ----------------------------
DROP TABLE IF EXISTS `ess_appconfig`;
CREATE TABLE `ess_appconfig` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(128) DEFAULT NULL,
  `APPCONFIGKEY` varchar(128) DEFAULT NULL,
  `APPCONFIGVALUE` varchar(512) DEFAULT NULL,
  `DESCRIPTION` varchar(512) DEFAULT NULL,
  `VALUETYPE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ess_appconfig
-- ----------------------------

-- ----------------------------
-- Table structure for filecomments_0
-- ----------------------------
DROP TABLE IF EXISTS `filecomments_0`;
CREATE TABLE `filecomments_0` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `FILEID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `COMMENTTIME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FILEID` (`FILEID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `filecomments_0_ibfk_1` FOREIGN KEY (`FILEID`) REFERENCES `files_0` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `filecomments_0_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of filecomments_0
-- ----------------------------

-- ----------------------------
-- Table structure for filepraise_0
-- ----------------------------
DROP TABLE IF EXISTS `filepraise_0`;
CREATE TABLE `filepraise_0` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `FILEID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FILEID` (`FILEID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `filepraise_0_ibfk_1` FOREIGN KEY (`FILEID`) REFERENCES `files_0` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `filepraise_0_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of filepraise_0
-- ----------------------------

-- ----------------------------
-- Table structure for files_0
-- ----------------------------
DROP TABLE IF EXISTS `files_0`;
CREATE TABLE `files_0` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `CLASSID` int(8) NOT NULL,
  `FILENAME` varchar(200) DEFAULT NULL,
  `CREATOR` int(8) NOT NULL,
  `CREATETIME` varchar(30) DEFAULT NULL,
  `SIZE` varchar(30) DEFAULT NULL,
  `MD5` varchar(32) DEFAULT NULL,
  `TYPE` varchar(20) DEFAULT NULL,
  `PRAISECOUNT` int(8) DEFAULT NULL,
  `COLLECTCOUNT` int(8) DEFAULT NULL,
  `ISDELETE` int(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CLASSID` (`CLASSID`),
  KEY `CREATOR` (`CREATOR`),
  CONSTRAINT `files_0_ibfk_1` FOREIGN KEY (`CLASSID`) REFERENCES `documentclass` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `files_0_ibfk_2` FOREIGN KEY (`CREATOR`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of files_0
-- ----------------------------

-- ----------------------------
-- Table structure for fileuserrelation
-- ----------------------------
DROP TABLE IF EXISTS `fileuserrelation`;
CREATE TABLE `fileuserrelation` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `COMPANYID` int(8) NOT NULL,
  `FILEID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  `GROUPID` int(8) NOT NULL,
  `ISCOLLECT` int(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPANYID` (`COMPANYID`),
  KEY `USERID` (`USERID`),
  KEY `GROUPID` (`GROUPID`),
  CONSTRAINT `fileuserrelation_ibfk_1` FOREIGN KEY (`COMPANYID`) REFERENCES `company` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fileuserrelation_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fileuserrelation_ibfk_3` FOREIGN KEY (`GROUPID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fileuserrelation
-- ----------------------------

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `COMPANYID` int(8) NOT NULL,
  `GROUPNAME` varchar(50) DEFAULT NULL,
  `REMARK` varchar(256) DEFAULT NULL,
  `CREATETIME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `COMPANYID` (`COMPANYID`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`COMPANYID`) REFERENCES `company` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of groups
-- ----------------------------

-- ----------------------------
-- Table structure for groupusersrelation
-- ----------------------------
DROP TABLE IF EXISTS `groupusersrelation`;
CREATE TABLE `groupusersrelation` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `GROUPID` int(8) NOT NULL,
  `USERID` int(8) NOT NULL,
  `JOINTIME` varchar(30) DEFAULT NULL,
  `ISADMIN` int(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `GROUPID` (`GROUPID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `groupusersrelation_ibfk_1` FOREIGN KEY (`GROUPID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `groupusersrelation_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of groupusersrelation
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `COMPANYID` int(8) NOT NULL,
  `USERNAME` varchar(100) DEFAULT NULL,
  `FULLNAME` varchar(20) DEFAULT NULL,
  `PASSWORD` varchar(50) DEFAULT NULL,
  `PORTRAIT` varchar(100) DEFAULT NULL,
  `TELEOHONE` varchar(13) DEFAULT NULL,
  `MOBILEPHONE` varchar(11) DEFAULT NULL,
  `FAX` varchar(13) DEFAULT NULL,
  `STATUS` int(1) DEFAULT NULL,
  `ENABLED` int(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `users_ibfk_1` (`COMPANYID`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`COMPANYID`) REFERENCES `company` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------

-- ----------------------------
-- Table structure for wechatset
-- ----------------------------
DROP TABLE IF EXISTS `wechatset`;
CREATE TABLE `wechatset` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `USERID` int(8) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`),
  CONSTRAINT `wechatset_ibfk_1` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechatset
-- ----------------------------

-- ----------------------------
-- Table structure for `emailattachments`
-- ----------------------------
DROP TABLE IF EXISTS `emailattachments`;
CREATE TABLE `emailattachments` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `USERID` int(8) NOT NULL,
  `EMAILINDEX` int(8) NOT NULL,
  `EMAIL` varchar(300) DEFAULT NULL,
  `SUBJECT` varchar(500) DEFAULT NULL,
  `SUBJECTTITLE` varchar(1000) DEFAULT NULL,
  `SENDER` varchar(800) DEFAULT NULL,
  `RECEIVER` varchar(800) DEFAULT NULL,
  `SENDTIME` varchar(30) DEFAULT NULL,
  `MAILTEXT` varchar(800) DEFAULT NULL,
  `CONTAINATTACHMENT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emailattachments
-- ----------------------------

-- ----------------------------
-- Table structure for `emailattachments`
-- ----------------------------
DROP TABLE IF EXISTS `EMAILDEFAULT`;
CREATE TABLE `EMAILDEFAULT` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `USERID` int(8) NOT NULL,
  `EMAIL` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emailattachments
-- ----------------------------

