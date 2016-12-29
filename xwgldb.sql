/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : xwgldb

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-12-03 23:18:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bm
-- ----------------------------
DROP TABLE IF EXISTS `bm`;
CREATE TABLE `bm` (
  `bmid` int(11) NOT NULL DEFAULT '0',
  `fbmid` int(11) NOT NULL DEFAULT '0',
  `bmmc` char(30) DEFAULT NULL,
  `bmms` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bmid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bm
-- ----------------------------
INSERT INTO `bm` VALUES ('0', '-1', '**公司', '**公司的描述');
INSERT INTO `bm` VALUES ('1', '0', 'A部门', 'A部门的描述');
INSERT INTO `bm` VALUES ('2', '1', 'B部门', 'B部门的描述');
INSERT INTO `bm` VALUES ('3', '1', 'C部门', 'C部门的描述');
INSERT INTO `bm` VALUES ('4', '1', 'D部门', 'D部门的描述');
INSERT INTO `bm` VALUES ('5', '2', 'E部门', 'E部门的描述');
INSERT INTO `bm` VALUES ('6', '2', 'F部门', 'F部门的描述');
INSERT INTO `bm` VALUES ('7', '3', 'G部门', 'G部门的描述');
INSERT INTO `bm` VALUES ('8', '3', 'K部门', 'K部门的描述');
INSERT INTO `bm` VALUES ('9', '4', 'I部门', 'I部门的描述');

-- ----------------------------
-- Table structure for fbzt
-- ----------------------------
DROP TABLE IF EXISTS `fbzt`;
CREATE TABLE `fbzt` (
  `fbztid` int(11) NOT NULL DEFAULT '0',
  `fbztmc` varchar(30) NOT NULL,
  PRIMARY KEY (`fbztid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fbzt
-- ----------------------------
INSERT INTO `fbzt` VALUES ('0', '未发布');
INSERT INTO `fbzt` VALUES ('1', '已发布');
INSERT INTO `fbzt` VALUES ('2', '已过期');

-- ----------------------------
-- Table structure for gly
-- ----------------------------
DROP TABLE IF EXISTS `gly`;
CREATE TABLE `gly` (
  `glyid` int(11) NOT NULL DEFAULT '0',
  `glydl` varchar(50) NOT NULL,
  `glyxm` varchar(50) DEFAULT NULL,
  `glymm` varchar(50) NOT NULL,
  `jsid` int(11) NOT NULL DEFAULT '0',
  `lxfs` char(30) DEFAULT NULL,
  PRIMARY KEY (`glyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gly
-- ----------------------------
INSERT INTO `gly` VALUES ('0', 'root', '超级管理员', '123', '0', '1503250000');

-- ----------------------------
-- Table structure for js
-- ----------------------------
DROP TABLE IF EXISTS `js`;
CREATE TABLE `js` (
  `jsid` int(11) NOT NULL DEFAULT '0',
  `jsmc` varchar(50) NOT NULL,
  PRIMARY KEY (`jsid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of js
-- ----------------------------
INSERT INTO `js` VALUES ('0', '系统管理员');
INSERT INTO `js` VALUES ('1', '系统主编');
INSERT INTO `js` VALUES ('2', '系统编辑');

-- ----------------------------
-- Table structure for lm
-- ----------------------------
DROP TABLE IF EXISTS `lm`;
CREATE TABLE `lm` (
  `lmid` int(11) NOT NULL DEFAULT '0',
  `lmmc` varchar(50) NOT NULL,
  `sxid` int(11) DEFAULT NULL,
  PRIMARY KEY (`lmid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lm
-- ----------------------------
INSERT INTO `lm` VALUES ('0', '未发布', '0');
INSERT INTO `lm` VALUES ('1', '新闻', '1');
INSERT INTO `lm` VALUES ('2', '财经', '2');
INSERT INTO `lm` VALUES ('3', '科技', '3');
INSERT INTO `lm` VALUES ('4', '体育', '4');
INSERT INTO `lm` VALUES ('5', '娱乐', '5');

-- ----------------------------
-- Table structure for qx
-- ----------------------------
DROP TABLE IF EXISTS `qx`;
CREATE TABLE `qx` (
  `qxid` int(11) NOT NULL,
  `qxmc` varchar(50) NOT NULL,
  PRIMARY KEY (`qxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qx
-- ----------------------------
INSERT INTO `qx` VALUES ('1', '增加新闻');
INSERT INTO `qx` VALUES ('2', '个人新闻管理');
INSERT INTO `qx` VALUES ('3', '查看发布新闻');
INSERT INTO `qx` VALUES ('4', '审核管理');
INSERT INTO `qx` VALUES ('5', '栏目管理');
INSERT INTO `qx` VALUES ('6', '基本权限查看');
INSERT INTO `qx` VALUES ('7', '角色权限管理');
INSERT INTO `qx` VALUES ('8', '部门管理');
INSERT INTO `qx` VALUES ('9', '员工信息管理');
INSERT INTO `qx` VALUES ('10', '个人信息管理');

-- ----------------------------
-- Table structure for qxfp
-- ----------------------------
DROP TABLE IF EXISTS `qxfp`;
CREATE TABLE `qxfp` (
  `jsid` int(11) NOT NULL DEFAULT '0',
  `qxid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`jsid`,`qxid`),
  KEY `qxid` (`qxid`),
  CONSTRAINT `qxfp_ibfk_1` FOREIGN KEY (`jsid`) REFERENCES `js` (`jsid`),
  CONSTRAINT `qxfp_ibfk_2` FOREIGN KEY (`qxid`) REFERENCES `qx` (`qxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qxfp
-- ----------------------------
INSERT INTO `qxfp` VALUES ('0', '1');
INSERT INTO `qxfp` VALUES ('1', '1');
INSERT INTO `qxfp` VALUES ('2', '1');
INSERT INTO `qxfp` VALUES ('0', '2');
INSERT INTO `qxfp` VALUES ('1', '2');
INSERT INTO `qxfp` VALUES ('2', '2');
INSERT INTO `qxfp` VALUES ('0', '3');
INSERT INTO `qxfp` VALUES ('1', '3');
INSERT INTO `qxfp` VALUES ('2', '3');
INSERT INTO `qxfp` VALUES ('0', '4');
INSERT INTO `qxfp` VALUES ('1', '4');
INSERT INTO `qxfp` VALUES ('0', '5');
INSERT INTO `qxfp` VALUES ('1', '5');
INSERT INTO `qxfp` VALUES ('0', '6');
INSERT INTO `qxfp` VALUES ('0', '7');
INSERT INTO `qxfp` VALUES ('0', '8');
INSERT INTO `qxfp` VALUES ('0', '9');
INSERT INTO `qxfp` VALUES ('0', '10');
INSERT INTO `qxfp` VALUES ('1', '10');
INSERT INTO `qxfp` VALUES ('2', '10');

-- ----------------------------
-- Table structure for shjl
-- ----------------------------
DROP TABLE IF EXISTS `shjl`;
CREATE TABLE `shjl` (
  `shid` int(11) NOT NULL DEFAULT '0',
  `xwid` int(11) NOT NULL DEFAULT '0',
  `tjr` int(11) NOT NULL,
  `tjsj` datetime DEFAULT NULL,
  `shrmc` varchar(50) DEFAULT NULL,
  `shsj` datetime DEFAULT NULL,
  `shyj` varchar(800) DEFAULT NULL,
  `ztid` int(11) NOT NULL,
  PRIMARY KEY (`shid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shjl
-- ----------------------------
INSERT INTO `shjl` VALUES ('1', '1', '0', '2016-07-01 10:16:37', '超级管理员', '2016-07-01 10:18:32', '通过', '3');
INSERT INTO `shjl` VALUES ('2', '2', '0', '2016-07-01 10:44:20', '超级管理员', '2016-07-01 10:18:32', ' 通过', '3');
INSERT INTO `shjl` VALUES ('3', '3', '0', '2016-07-04 14:20:34', '超级管理员', '2016-07-04 14:21:15', ' sdfsdf', '3');

-- ----------------------------
-- Table structure for tp
-- ----------------------------
DROP TABLE IF EXISTS `tp`;
CREATE TABLE `tp` (
  `tpid` int(11) NOT NULL DEFAULT '0',
  `tpms` varchar(50) DEFAULT NULL,
  `xwid` int(11) NOT NULL DEFAULT '0',
  `tplj` varchar(50) DEFAULT NULL,
  `tplx` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`tpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tp
-- ----------------------------
INSERT INTO `tp` VALUES ('1', null, '1', '1_titel.jpg', '0');
INSERT INTO `tp` VALUES ('2', 'y', '1', '1_pic_1.jpg', '1');
INSERT INTO `tp` VALUES ('3', 'yutyu', '1', '1_pic_2.jpg', '2');
INSERT INTO `tp` VALUES ('4', null, '2', '2_titel.jpg', '0');
INSERT INTO `tp` VALUES ('5', '哎', '2', '2_pic_1.jpg', '1');
INSERT INTO `tp` VALUES ('6', null, '3', '3_titel.jpg', '0');

-- ----------------------------
-- Table structure for xw
-- ----------------------------
DROP TABLE IF EXISTS `xw`;
CREATE TABLE `xw` (
  `xwid` int(11) NOT NULL DEFAULT '1',
  `xwbt` varchar(50) NOT NULL,
  `xwgs` varchar(200) DEFAULT NULL,
  `xwly` varchar(30) DEFAULT NULL,
  `fbsj` datetime DEFAULT NULL,
  `xwnr` varchar(10000) NOT NULL,
  `bsid` int(11) NOT NULL,
  `lmid` int(11) DEFAULT NULL,
  `ygid` int(11) NOT NULL,
  `ztid` int(11) NOT NULL,
  `fbztid` int(11) NOT NULL,
  `sxid` int(11) DEFAULT NULL,
  PRIMARY KEY (`xwid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xw
-- ----------------------------
INSERT INTO `xw` VALUES ('1', 'tr', 'ert', 'et', '2016-07-01 10:16:37', 'ghjghjghjghjytttttttttttttt', '3', '1', '0', '3', '1', '1');
INSERT INTO `xw` VALUES ('2', '紫轩阁', '一片飘绿，就那么点红', '自选股', '2016-07-01 10:44:20', '一片飘绿，就那么点红', '2', '2', '0', '3', '1', '1');
INSERT INTO `xw` VALUES ('3', 'sdf', 'sdfdsf', 'dsfsdf', '2016-07-04 14:20:34', 'fdsfsdfsdfsdfsdfsdf', '1', '3', '0', '3', '1', '1');

-- ----------------------------
-- Table structure for xwzt
-- ----------------------------
DROP TABLE IF EXISTS `xwzt`;
CREATE TABLE `xwzt` (
  `ztid` int(11) NOT NULL DEFAULT '0',
  `ztmc` varchar(30) NOT NULL,
  PRIMARY KEY (`ztid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xwzt
-- ----------------------------
INSERT INTO `xwzt` VALUES ('0', '未提交审核');
INSERT INTO `xwzt` VALUES ('1', '提交未审核');
INSERT INTO `xwzt` VALUES ('2', '未通过审核');
INSERT INTO `xwzt` VALUES ('3', '通过审核');
INSERT INTO `xwzt` VALUES ('4', '封杀');

-- ----------------------------
-- Table structure for yg
-- ----------------------------
DROP TABLE IF EXISTS `yg`;
CREATE TABLE `yg` (
  `ygid` int(11) NOT NULL DEFAULT '0',
  `ygdl` varchar(50) NOT NULL,
  `ygxm` varchar(50) DEFAULT NULL,
  `ygxb` varchar(50) DEFAULT NULL,
  `ygmm` varchar(50) NOT NULL,
  `lxfs` varchar(150) DEFAULT NULL,
  `bmid` int(11) NOT NULL DEFAULT '0',
  `jsid` int(11) NOT NULL DEFAULT '0',
  `lzyf` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ygid`),
  KEY `jsid` (`jsid`),
  CONSTRAINT `yg_ibfk_1` FOREIGN KEY (`jsid`) REFERENCES `js` (`jsid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yg
-- ----------------------------
INSERT INTO `yg` VALUES ('0', 'rlfz', '如来佛祖', '男', '123', '10086', '1', '0', '0');
INSERT INTO `yg` VALUES ('1', 'swk', '孙悟空', '男', 'swk', '1008611', '2', '2', '0');
INSERT INTO `yg` VALUES ('2', 'bgj', '白骨精', '男', 'bgj', '12580', '3', '2', '0');
INSERT INTO `yg` VALUES ('3', 'ts', '唐僧', '男', 'ts', '110', '1', '1', '0');
INSERT INTO `yg` VALUES ('4', 'zbj', '猪八戒', '男', 'zbj', '119', '1', '2', '0');
INSERT INTO `yg` VALUES ('5', 'nmw', '牛魔王', '男', 'nmw', '008', '3', '2', '0');
