/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80409
 Source Host           : localhost:3306
 Source Schema         : xh-mingpianfenxiang

 Target Server Type    : MySQL
 Target Server Version : 80409
 File Encoding         : 65001

 Date: 20/11/2026 14:36:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `address_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户姓名',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户电话',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '市',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '区',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户详细地址（用户手动输入的地址）',
  `lng` double(20, 10) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(20, 10) NULL DEFAULT NULL COMMENT '维度',
  `delete_flag` int(0) NULL DEFAULT NULL COMMENT '是否删除（0：未删除；1：删除）',
  `address_default` int(0) NULL DEFAULT NULL COMMENT '默认地址 0：非默认   1：默认',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`address_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `android_wgt_url` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_at` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `des` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `ios_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `ios_wgt_url` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `method` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `version` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `wgt_url` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES (5, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', NULL, '', 'false', '1.1', 'https://www.pgyer.com/hb2v');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'bannerid',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片地址',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(0) NULL DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(0) NULL DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `type` int(0) NULL DEFAULT NULL COMMENT '分类 1首页 2好店',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '2024-03-14 18:40:41', '轮播', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/13/f8ebb98e25300cfdb441511ab5a0296e.png', 1, 1, 'https://taobao.xianmxkj.com/custom.jpg', 1, '轮播', NULL);
INSERT INTO `banner` VALUES (10, '2024-03-18 10:08:55', '游戏', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/8bf66890dca4224ca095f4f900d36c15.png', 1, 2, '/pages/index/classify/index?industrySecondName=游戏', NULL, '游戏', 1);
INSERT INTO `banner` VALUES (11, '2023-07-13 16:09:12', '家电维修', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/9c79b33e548d85003b66e24273651ebb.png', 1, 2, '/pages/index/classify/index?classId=9&className=家电维修', NULL, '家电维修', 1);
INSERT INTO `banner` VALUES (12, '2023-07-13 16:09:12', '智能家装', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/40a24ad68857e9cd18de957686bed167.png', 1, 2, '/pages/index/classify/index?classId=10&className=智能家装', NULL, '智能家装', 1);
INSERT INTO `banner` VALUES (13, '2023-05-24 17:03:56', '精选搬家', 'https://pw.xianmxkj.com/file/uploadPath/2022/03/22/367c0cdcd429213624b9144278e30d86.png', 1, 2, '/pages/index/classify/index', NULL, '精选搬家', NULL);
INSERT INTO `banner` VALUES (15, '2023-07-13 16:09:12', '宠物寄养', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/2cf11dd8ea10236f9c815bdd594bffbd.png', 1, 2, '/pages/index/classify/index?classId=12&className=宠物寄养', NULL, '宠物寄养', 1);
INSERT INTO `banner` VALUES (19, '2026-03-18 11:14:19', '邀请背景图', 'https://mpfx.xianmaxiong.com/file/uploadPath/2026/03/18/b44bbe392bece0a0632ba687c918db15.png', 1, 5, 'https://peiwan.xianmxkj.com', NULL, '邀请背景图', NULL);
INSERT INTO `banner` VALUES (20, '2023-07-13 16:09:12', '休闲娱乐', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/4c38bfcbfc61c0feac5bb77ab1a40911.png', 1, 2, '/pages/index/classify/index?classId=13&className=娱乐休闲', NULL, '休闲娱乐', 1);
INSERT INTO `banner` VALUES (21, '2023-07-13 16:09:12', '餐饮美食', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/1218e8393dfcbc0d038d6afce10dfb55.png', 1, 2, '/pages/index/classify/index?classId=14&className=餐饮美食', NULL, '餐饮美食', 1);
INSERT INTO `banner` VALUES (22, '2023-07-13 16:09:12', '汽车服务', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/df793d701aa8d66341d21a9ab1aa9b76.png', 1, 2, '/pages/index/classify/index?classId=15&className=汽车服务', NULL, '汽车服务', 1);
INSERT INTO `banner` VALUES (23, '2023-07-13 16:09:12', '二手回收', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/46567c35bea1a43896f891ed99e03348.png', 1, 2, '/pages/index/classify/index?classId=16&className=二手回收', NULL, '二手回收', 1);
INSERT INTO `banner` VALUES (24, '2023-07-13 14:07:36', '票务卡劵', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/abc9ed1a766026ee22103f0fbb4f9ebe.png', 1, 2, '/pages/index/classify/index?classId=24&className=票务卡劵', NULL, '票务卡劵', 1);
INSERT INTO `banner` VALUES (26, '2022-01-24 15:34:25', '测试', 'https://pw.xianmxkj.com/file/uploadPath/2022/01/24/b8f6cac9057770b2d7044f43032534d7.png', 1, 6, 'sd', NULL, '111', NULL);
INSERT INTO `banner` VALUES (27, '2023-07-13 18:58:51', '对对对', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/13/aed762e42d9dc95879fb1866023204aa.png', 1, 7, 'https', NULL, '订单', NULL);
INSERT INTO `banner` VALUES (28, '2023-07-13 14:07:36', '酒店旅游', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/228f6b0511316d2e9c56726c7f723571.png', 1, 2, '/pages/index/classify/index?classId=28&className=酒店旅游', NULL, '酒店旅游', 2);
INSERT INTO `banner` VALUES (30, '2023-07-13 14:07:36', '购物天地', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/5e1d1d84f28cec023bff1d7dbb07ea29.png', 1, 2, '/pages/index/classify/index?classId=30&className=购物天地', NULL, '购物天地', 2);
INSERT INTO `banner` VALUES (31, '2023-07-13 14:07:36', '生活服务', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/b9bb18c99660a43d3c4bd4847ac1073a.png', 1, 2, '/pages/index/classify/index?classId=31&className=生活服务', NULL, '生活服务', 2);
INSERT INTO `banner` VALUES (32, '2023-07-13 14:07:36', '医学美容', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/0d5388bc9d24b37069230b691ae7b3c6.png', 1, 2, '/pages/index/classify/index?classId=32&className=医学美容', NULL, '医学美容', 2);
INSERT INTO `banner` VALUES (33, '2023-07-13 14:07:36', '宠物服务', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/c05279c507ff18399a0696bc3a740f5b.png', 1, 2, '/pages/index/classify/index?classId=33&className=宠物服务', NULL, '宠物服务', 2);
INSERT INTO `banner` VALUES (34, '2023-07-13 14:07:36', '休闲娱乐', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/88467a71db83cab40527b6ac47806279.png', 1, 2, '/pages/index/classify/index?classId=34&className=休闲娱乐', NULL, '休闲娱乐', 2);
INSERT INTO `banner` VALUES (35, '2023-07-13 14:07:36', '餐饮美食', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/02b7bcb7ceda7d1f08db0f059731f097.png', 1, 2, '/pages/index/classify/index?classId=35&className=餐饮美食', NULL, '餐饮美食', 2);
INSERT INTO `banner` VALUES (36, '2023-07-13 14:07:36', '汽车服务', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/07122fb97a8138465edf176a4f376194.png', 1, 2, '/pages/index/classify/index?classId=36&className=汽车服务', NULL, '汽车服务', 2);
INSERT INTO `banner` VALUES (37, '2023-07-13 14:07:36', '亲子乐园', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/eafe9236f5c3416dac0ece1f62110d38.png', 1, 2, '/pages/index/classify/index?classId=37&className=亲子乐园', NULL, '亲子乐园', 2);
INSERT INTO `banner` VALUES (38, '2023-07-13 14:07:36', '拍照摄影', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/1d6df3fb850cca409b5de2aac53f7e7f.png', 1, 2, '/pages/index/classify/index?classId=38&className=拍照摄影', NULL, '拍照摄影', 2);
INSERT INTO `banner` VALUES (39, '2023-07-13 14:07:36', '休闲娱乐', 'https://tcxx.xianmaxiong.com/file/uploadPath/2023/07/12/0f21e0368248c34768a68485b7431e71.png', 1, 2, '/pages/index/classify/index?classId=39&className=休闲娱乐', NULL, '休闲娱乐', 1);
INSERT INTO `banner` VALUES (42, '2023-07-17 17:46:12', '查看访客详情', 'https://oss.dzgqb.com/20260304/930229d1f6914d12af9d3c73cd8a62b5.png', 1, 10, NULL, NULL, '随时查看谁看过你', NULL);
INSERT INTO `banner` VALUES (44, '2023-07-17 18:23:03', '名片数量不上限', 'https://oss.dzgqb.com/20260304/7b52980ca26744e698030416d29f7fe3.png', 1, 10, NULL, NULL, '创建名片数量不上限', NULL);
INSERT INTO `banner` VALUES (45, '2023-07-17 18:23:20', '查看认证员工', 'https://oss.dzgqb.com/20260304/9388e812baf44e409df375c0b9a03468.png', 1, 10, NULL, NULL, '随时可看员工信息', NULL);
INSERT INTO `banner` VALUES (46, '2023-07-17 18:23:35', '增长名片人气值', 'https://oss.dzgqb.com/20260304/adf67fa11eea4cd8a7dd4008ce1a96d8.png', 1, 10, NULL, NULL, '让更多用户看到你', NULL);
INSERT INTO `banner` VALUES (49, '2024-03-11 18:05:25', 'asd', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/11/e69c8a710c4427355a27522efbd58f73.png', 1, 8, '3', NULL, 'd', NULL);
INSERT INTO `banner` VALUES (50, '2024-03-11 18:05:54', '分类1', NULL, 2, 2, NULL, NULL, NULL, NULL);
INSERT INTO `banner` VALUES (51, '2024-03-11 18:07:29', '分类1', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/11/b1bc6a14d35b6898f1fb2500be1077e1.png', 1, 9, NULL, NULL, NULL, NULL);
INSERT INTO `banner` VALUES (52, '2024-03-11 18:10:54', '345', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/11/8b7704c0a9d6d9aba0f93bc7f40d7172.png', 1, 11, '4', NULL, NULL, NULL);
INSERT INTO `banner` VALUES (53, '2024-03-11 18:10:44', '分类2', NULL, 1, 12, '4', NULL, '4', NULL);
INSERT INTO `banner` VALUES (54, '2024-03-14 18:25:40', '7', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/14/e046e3ebbbe9e0e003175fc0e5067ce2.jpg', 1, 8, '7', NULL, '7', 2);
INSERT INTO `banner` VALUES (55, '2024-03-18 10:08:55', '游戏', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/14/fbd2be56ed3221e9394b0e75f65d9100.png', 1, 9, '/pages/haodian/classify/index?industrySecondName=游戏', NULL, '游戏', 2);
INSERT INTO `banner` VALUES (56, '2024-03-14 18:27:34', '6', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/14/489f9aa040f64ef7673fd88f74a313ed.png', 1, 11, '7', NULL, '6', 3);
INSERT INTO `banner` VALUES (57, '2024-04-01 15:27:33', '咨询', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/14/6f643beabf6c351a799c698f32f9236a.png', 1, 12, '/my/industry/industry?industryType=咨询', NULL, '咨询', 3);
INSERT INTO `banner` VALUES (58, '2024-03-14 18:40:03', '4', 'https://tcxx.xianmaxiong.com/file/uploadPath/2024/03/14/248965c0804e13f1c45b7b524371a1f0.png', 1, 1, '4', NULL, '4', 1);
INSERT INTO `banner` VALUES (59, '2024-04-01 15:28:04', 'banner', 'https://wap.dzgqb.com/file/uploadPath/2024/04/01/cb4962314d9f411cabf790e5e92b4750.png', 1, 14, 'https://baidu.com', NULL, 'banner', 2);
INSERT INTO `banner` VALUES (60, '2024-04-01 15:27:33', 'banner', 'https://wap.dzgqb.com/file/uploadPath/2024/04/01/13913671a9c350b2f7869c513f618b61.png', 1, 15, 'https://baidu.com', NULL, 'banner', 3);

-- ----------------------------
-- Table structure for business_card
-- ----------------------------
DROP TABLE IF EXISTS `business_card`;
CREATE TABLE `business_card`  (
  `card_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '名片id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `card_type` int(0) NULL DEFAULT NULL COMMENT '名片类型 1=个人名片 2企业名片',
  `card_avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '用户头像',
  `card_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '真实姓名',
  `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司名称',
  `company_position` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '所属岗位(字典)',
  `company_province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司省',
  `company_city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司市',
  `company_district` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司区',
  `company_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司详细地址',
  `company_area` double(10, 2) NULL DEFAULT NULL COMMENT '面积(单位:平方米)',
  `company_logo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司logo',
  `company_output_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '年产值(单位:万元)',
  `company_website` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司官网',
  `company_flair_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '资质认证图片',
  `company_flair` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '资质认证名称',
  `company_blurb` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司简介',
  `first_product` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '主营产品',
  `first_product_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '主营产品图',
  `company_ambient` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司环境',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `card_status` int(0) NULL DEFAULT 0 COMMENT '名片状态 0不展示  1展示',
  `audit_status` int(0) NULL DEFAULT 0 COMMENT '名片审核状态 0待审核 1审核成功 2审核未通过',
  `examine_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '审核备注',
  `browse_count` int(0) NULL DEFAULT 0 COMMENT '浏览次数',
  `industry_first_id` int(0) NULL DEFAULT NULL COMMENT '行业一级id',
  `industry_first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '行业一级名称',
  `industry_second_id` int(0) NULL DEFAULT NULL COMMENT '行业二级id',
  `industry_second_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '行业二级名称',
  `card_style_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名片样式code',
  `card_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名片照片',
  `share_count` int(0) NULL DEFAULT 0 COMMENT '分享次数',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父id',
  `popularity_value` int(0) NULL DEFAULT 0 COMMENT '人气值',
  `legal_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '法人',
  `registered_capital` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '注册资金',
  `registered_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `company_license` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '营业执照',
  `company_num` int(0) NULL DEFAULT NULL COMMENT '公司人数',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `work_experience` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '工作经历',
  `edu_experience` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '教育经历',
  `card_img_back` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '名片图像背面',
  `quota` int(0) NULL DEFAULT NULL COMMENT '额度',
  `used_quota` int(0) NULL DEFAULT NULL COMMENT '已使用额度',
  `wx_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信号',
  PRIMARY KEY (`card_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '名片信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business_card
-- ----------------------------

-- ----------------------------
-- Table structure for card_style
-- ----------------------------
DROP TABLE IF EXISTS `card_style`;
CREATE TABLE `card_style`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `style_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式编码',
  `style_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式名称',
  `style_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式图片',
  `sort_id` int(0) NULL DEFAULT NULL COMMENT '序号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_free` int(0) NULL DEFAULT NULL COMMENT '是否免费 0免费 1VIP免费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '名片样式' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of card_style
-- ----------------------------
INSERT INTO `card_style` VALUES ('22f261863b394ceb9f307f2e188459da', '000000', '样式1', 'https://oss.dzgqb.com/20260202/de7078c449e14c49a1b1c51f344423c7.jpg', 1, '2025-11-21 10:44:01', NULL);
INSERT INTO `card_style` VALUES ('e766c3f1057e4d309f145a3d599f135a', 'default', '默认样式', 'https://huoyun.xianmaxiong.com/file/uploadPath/2025/12/05/4dfbbdc231fb52ff3b6a4e51de89ffe3.jpg', 1, '2024-05-06 17:42:23', 0);

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '申请时间',
  `is_out` int(0) NULL DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单编号',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款原因',
  `classify` int(0) NULL DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信图片',
  `rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '利息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cash_out
-- ----------------------------

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content`  (
  `chat_content_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '聊天内容id',
  `chat_conversation_id` int(0) NULL DEFAULT NULL COMMENT '聊天会话id',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `message_type` int(0) NULL DEFAULT NULL COMMENT '类型 1文字 2图片 3语音',
  `width` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片宽度',
  `height` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片高度',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '发送用户id',
  `status` int(0) NULL DEFAULT NULL COMMENT '0未读 1已读',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送时间',
  `voice_length` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 820 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_content
-- ----------------------------

-- ----------------------------
-- Table structure for chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat_conversation`;
CREATE TABLE `chat_conversation`  (
  `chat_conversation_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '聊天会话id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '发送用户id',
  `focused_user_id` int(0) NULL DEFAULT NULL COMMENT '接受用户id',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '状态',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `is_send_msg` int(0) NULL DEFAULT NULL,
  `is_wx_msg` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`chat_conversation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 243 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_conversation
-- ----------------------------

-- ----------------------------
-- Table structure for chats
-- ----------------------------
DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats`  (
  `chat_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '会话id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `user_count` int(0) NULL DEFAULT 0 COMMENT '用户未读条数',
  `user_count2` int(0) NULL DEFAULT 0 COMMENT '好友未读条数',
  `user_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户头像',
  `user_head2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '好友头像',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(0) NULL DEFAULT NULL COMMENT '好友id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户昵称',
  `user_name2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '好友昵称',
  `store_count` int(0) NULL DEFAULT 0 COMMENT '后台未读条数',
  `store_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '后台头像',
  `store_id` bigint(0) NULL DEFAULT NULL COMMENT '总后台id(总后台传0',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商户昵称',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '聊天会话' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats
-- ----------------------------

-- ----------------------------
-- Table structure for chats_content
-- ----------------------------
DROP TABLE IF EXISTS `chats_content`;
CREATE TABLE `chats_content`  (
  `chat_content_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '会话内容id',
  `chat_id` bigint(0) NULL DEFAULT NULL COMMENT '会话Id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '聊天内容',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `send_type` bigint(0) NULL DEFAULT NULL COMMENT '消息来源（1用户消息 2好友消息）',
  `status` int(0) NULL DEFAULT 1 COMMENT '是否已读(1未读 2已读)',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型（1文字 2图片 3链接）',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(0) NULL DEFAULT NULL COMMENT '好友id',
  `store_id` bigint(0) NULL DEFAULT NULL COMMENT '后台id（总后台传0）',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '聊天会话内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats_content
-- ----------------------------

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `min` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置文件名称',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '值',
  `condition_from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 432 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` VALUES (1, '2023-07-18 09:40:54', NULL, '客服二维码', 1, '', 'xitong');
INSERT INTO `common_info` VALUES (2, '2020-02-21 21:17:17', NULL, '公众号二维码', 2, '', 'xitong');
INSERT INTO `common_info` VALUES (6, '2020-07-09 17:59:45', NULL, '微信公众号APPID	', 5, '', 'weixin');
INSERT INTO `common_info` VALUES (7, '2021-03-15 21:16:30', NULL, '短信签名', 6, '', 'duanxin');
INSERT INTO `common_info` VALUES (16, '2020-02-25 20:43:38', NULL, '微信公众号秘钥	', 21, '', 'weixin');
INSERT INTO `common_info` VALUES (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, '', 'weixin');
INSERT INTO `common_info` VALUES (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, '', 'weixin');
INSERT INTO `common_info` VALUES (20, '2023-07-17 15:26:13', NULL, '后台管理平台域名配置	', 20, '', 'xitong');
INSERT INTO `common_info` VALUES (21, '2023-07-07 13:32:18', NULL, 'h5服务域名配置	', 19, '', 'xitong');
INSERT INTO `common_info` VALUES (22, '2023-11-08 19:29:20', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '', 'duanxin');
INSERT INTO `common_info` VALUES (23, '2022-03-20 15:08:22', NULL, '后台服务名称	', 12, '', 'xitong');
INSERT INTO `common_info` VALUES (27, '2026-03-06 09:45:24', NULL, '通用APP下载地址	', 25, '', 'xitong');
INSERT INTO `common_info` VALUES (33, '2023-11-08 19:29:38', NULL, '腾讯云短信clientId	', 31, '', 'duanxin');
INSERT INTO `common_info` VALUES (34, '2023-11-08 19:29:43', NULL, '腾讯云短信clientSecret	', 32, '', 'duanxin');
INSERT INTO `common_info` VALUES (44, '2022-03-10 20:38:21', NULL, '客服微信号', 44, '', 'xitong');
INSERT INTO `common_info` VALUES (47, '2026-03-13 09:29:32', NULL, '微信小程序APPID', 45, '', 'weixin');
INSERT INTO `common_info` VALUES (48, '2026-03-13 09:29:47', NULL, '微信小程序秘钥', 46, '', 'weixin');
INSERT INTO `common_info` VALUES (51, '2020-03-29 00:21:00', NULL, '分享安卓下载地址', 49, '', 'xitong');
INSERT INTO `common_info` VALUES (52, '2020-03-29 00:21:00', NULL, '分享苹果下载地址', 50, '', 'xitong');
INSERT INTO `common_info` VALUES (58, '2020-03-29 00:21:00', NULL, '开启微信登录', 53, '', 'kaiguans');
INSERT INTO `common_info` VALUES (69, '2020-06-04 16:34:00', NULL, 'APP消息推送PushAppKey', 60, '', 'push');
INSERT INTO `common_info` VALUES (70, '2020-06-04 16:34:00', NULL, 'APP消息推送PushAppId', 61, '', 'push');
INSERT INTO `common_info` VALUES (71, '2020-06-04 16:34:00', NULL, 'APP消息推送PushMasterSecret', 62, '', 'push');
INSERT INTO `common_info` VALUES (72, '2020-06-04 16:34:00', NULL, '企业支付宝APPID', 63, '', 'zhifubao');
INSERT INTO `common_info` VALUES (73, '2020-06-04 16:34:00', NULL, '企业支付宝公钥', 64, '', 'zhifubao');
INSERT INTO `common_info` VALUES (74, '2020-06-04 16:34:00', NULL, '企业支付宝商户秘钥', 65, '', 'zhifubao');
INSERT INTO `common_info` VALUES (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, '', 'oss');
INSERT INTO `common_info` VALUES (78, '2021-03-15 21:22:38', NULL, '文件上传阿里云账号accessKeyId', 69, '', 'oss');
INSERT INTO `common_info` VALUES (79, '2021-03-15 21:22:46', NULL, '文件上传阿里云账号accessKeySecret', 70, '', 'oss');
INSERT INTO `common_info` VALUES (80, '2021-03-15 21:22:53', NULL, '文件上传阿里云Bucket名称', 71, '', 'oss');
INSERT INTO `common_info` VALUES (81, '2021-03-15 21:23:00', NULL, '文件上传阿里云Bucket域名', 72, '', 'oss');
INSERT INTO `common_info` VALUES (83, '2020-07-27 15:17:00', NULL, '微信APPappId', 74, '', 'weixin');
INSERT INTO `common_info` VALUES (84, '2020-07-27 15:17:00', NULL, '微信商户key', 75, '', 'weixin');
INSERT INTO `common_info` VALUES (85, '2020-07-27 15:17:00', NULL, '微信商户号mchId', 76, '', 'weixin');
INSERT INTO `common_info` VALUES (96, '2020-07-27 15:17:00', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 80, '', 'duanxin');
INSERT INTO `common_info` VALUES (97, '2020-07-27 15:17:00', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 81, '', 'duanxin');
INSERT INTO `common_info` VALUES (98, '2020-07-27 15:17:00', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 82, '', 'duanxin');
INSERT INTO `common_info` VALUES (99, '2020-07-27 15:17:00', NULL, '阿里云短信accessKeyId', 83, '', 'duanxin');
INSERT INTO `common_info` VALUES (100, '2020-07-27 15:17:00', NULL, '阿里云短信accessSecret', 84, '', 'duanxin');
INSERT INTO `common_info` VALUES (102, '2022-04-21 19:40:07', NULL, '转账方式 1支付宝证书  2支付宝秘钥 3手动', 98, '', 'zhifubao');
INSERT INTO `common_info` VALUES (108, '2023-07-14 16:40:21', NULL, '公众号是否自动登录', 108, '', 'kaiguans');
INSERT INTO `common_info` VALUES (118, '2020-07-27 15:17:00', NULL, '邀请推广内容', 101, '', 'xitongs');
INSERT INTO `common_info` VALUES (120, '2023-07-18 11:14:42', NULL, '邀请微信分享内容', 103, '', 'weixin');
INSERT INTO `common_info` VALUES (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '', 'fuwufei');
INSERT INTO `common_info` VALUES (134, '2023-07-18 11:04:46', NULL, '海报邀请语', 116, '', 'xitong');
INSERT INTO `common_info` VALUES (154, '2023-07-18 11:15:11', NULL, '是否开启APP微信分享', 136, '', 'kaiguan');
INSERT INTO `common_info` VALUES (159, '2023-07-18 11:15:15', NULL, 'H5推广是否分享APP下载页面', 141, '', 'kaiguan');
INSERT INTO `common_info` VALUES (170, '2022-03-17 18:03:01', NULL, '提现手续费（用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）', 152, '', 'fuwufei');
INSERT INTO `common_info` VALUES (171, '2021-09-06 16:19:51', NULL, '最高提现金额', 153, '', 'fuwufei');
INSERT INTO `common_info` VALUES (182, '2022-03-20 15:06:28', NULL, '短信宝用户名', 164, '', 'duanxin');
INSERT INTO `common_info` VALUES (183, '2026-04-07 18:04:40', NULL, '短信宝密码', 165, '', 'duanxin');
INSERT INTO `common_info` VALUES (200, '2021-07-19 11:32:12', NULL, '帮助中心', 175, '', 'xieyi');
INSERT INTO `common_info` VALUES (201, '2022-04-18 14:05:03', NULL, '隐私政策', 176, '', 'xieyi');
INSERT INTO `common_info` VALUES (202, '2021-08-14 19:09:54', NULL, '注册协议', 177, '', 'xieyi');
INSERT INTO `common_info` VALUES (203, '2021-08-14 19:09:54', NULL, '关于我们', 187, '', 'xieyi');
INSERT INTO `common_info` VALUES (204, '2023-07-18 09:36:04', NULL, '登录是否获取手机号', 188, '', 'kaiguan');
INSERT INTO `common_info` VALUES (217, '2020-07-27 15:17:00', NULL, '腾讯地图key', 217, '', 'xitong');
INSERT INTO `common_info` VALUES (234, '2020-11-04 10:31:40', NULL, '上传方式 1阿里云oss  2本地', 234, '', 'oss');
INSERT INTO `common_info` VALUES (240, '2020-11-04 10:31:40', NULL, '未读消息提醒', 240, '', 'weixin');
INSERT INTO `common_info` VALUES (241, '2020-11-04 10:31:40', NULL, '超时几分钟短信通知', 241, '', 'xitong');
INSERT INTO `common_info` VALUES (242, '2020-11-04 10:31:40', NULL, '几条未读短信通知', 242, '', 'xitong');
INSERT INTO `common_info` VALUES (243, '2020-11-04 10:31:40', NULL, '是否开启微信提现', 243, '', 'kaiguan');
INSERT INTO `common_info` VALUES (244, '2021-12-22 10:11:36', NULL, '微信提现方式 1自动 2手动', 244, '', 'xitong');
INSERT INTO `common_info` VALUES (245, '2020-11-04 10:31:40', NULL, '是否开启支付宝提现', 245, '', 'kaiguan');
INSERT INTO `common_info` VALUES (246, '2020-10-28 16:57:07', NULL, '微信证书地址', 201, '', 'weixin');
INSERT INTO `common_info` VALUES (257, '2020-11-04 10:31:40', NULL, '身份验证SecretID', 257, '', 'xitong');
INSERT INTO `common_info` VALUES (258, '2020-11-04 10:31:40', NULL, '身份验证SecretKey', 258, '', 'xitong');
INSERT INTO `common_info` VALUES (259, '2020-11-04 10:31:40', NULL, '身份验证实例ID', 259, '', 'xitong');
INSERT INTO `common_info` VALUES (262, '2026-03-13 09:35:04', NULL, '小程序是否上架', 262, '', 'kaiguan');
INSERT INTO `common_info` VALUES (276, '2023-10-20 16:46:12', NULL, '推广文案', 276, '', 'xitong');
INSERT INTO `common_info` VALUES (289, '2026-03-17 18:30:32', NULL, '推广图片', 289, '', 'image');
INSERT INTO `common_info` VALUES (290, '2023-07-10 10:32:30', NULL, '关键词过滤', 290, '', 'xitong');
INSERT INTO `common_info` VALUES (291, '2023-07-10 10:36:08', NULL, '聊天警告文字提醒', 291, '', 'xitong');
INSERT INTO `common_info` VALUES (292, '2023-07-10 17:31:30', NULL, '审核结果通知', 292, '', 'weixin');
INSERT INTO `common_info` VALUES (293, '2023-07-10 17:31:30', NULL, '用户注销协议', 293, '', 'xieyi');
INSERT INTO `common_info` VALUES (296, '2026-03-17 11:08:06', NULL, '客服类型(1:电话，2:企业微信，3:客服二维码)', 296, '', 'xitong');
INSERT INTO `common_info` VALUES (297, '2026-03-09 16:18:55', NULL, '企业微信客服APPID\r\n', 297, '', 'xitong');
INSERT INTO `common_info` VALUES (298, '2023-07-11 16:04:28', NULL, '客服电话', 298, '', 'xitong');
INSERT INTO `common_info` VALUES (300, '2026-03-09 16:18:48', NULL, '企业客户链接', 300, '', 'xitong');
INSERT INTO `common_info` VALUES (301, '2026-04-24 13:50:47', NULL, '小程序上线开关1', 301, '', 'kaiguan');
INSERT INTO `common_info` VALUES (302, '2026-04-09 15:30:40', NULL, '小程序上线开关2', 302, '', 'kaiguan');
INSERT INTO `common_info` VALUES (303, '2023-07-12 09:42:00', NULL, '是否开启分佣提成', 303, '', 'kaiguan');
INSERT INTO `common_info` VALUES (309, '2023-07-17 15:23:54', NULL, '用户小程序名称配置', 309, '', 'xitong');
INSERT INTO `common_info` VALUES (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '', 'xitong');
INSERT INTO `common_info` VALUES (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, '', 'xitong');
INSERT INTO `common_info` VALUES (416, '2023-07-26 16:27:15', NULL, '积分与点数的兑换比例(1积分等于多少点数)', 416, '', 'xitongs');
INSERT INTO `common_info` VALUES (418, '2024-02-28 20:13:27', NULL, '查看采购信息代理点数分成比例(0.1表示10%)', 310, '', 'xitongs');
INSERT INTO `common_info` VALUES (419, '2024-02-28 20:13:27', NULL, '代理分成下级积分比例(0.1表示10%)', 311, '', 'xitongs');
INSERT INTO `common_info` VALUES (420, '2024-03-15 11:08:49', NULL, '联系发布微信二维码', 312, '', 'xitong');
INSERT INTO `common_info` VALUES (421, '2024-03-15 11:10:39', NULL, '联系发布手机号', 313, '', 'xitong');
INSERT INTO `common_info` VALUES (422, '2026-02-26 11:38:08', NULL, '企业默认额度', 314, '', 'xitong');
INSERT INTO `common_info` VALUES (423, '2026-01-14 13:10:23', NULL, '会员服务协议', 315, '', 'xieyi');
INSERT INTO `common_info` VALUES (424, '2026-03-05 18:51:03', NULL, '分销比例', 316, '', 'xitong');
INSERT INTO `common_info` VALUES (425, '2026-03-06 16:17:40', NULL, '百度api key', 507, '', 'xitong');
INSERT INTO `common_info` VALUES (426, '2026-03-06 16:18:24', NULL, '百度云secretKey', 508, '', 'xitong');
INSERT INTO `common_info` VALUES (427, '2026-03-09 14:48:08', NULL, '默认名片背景', 317, '', 'image');
INSERT INTO `common_info` VALUES (428, '2026-03-09 18:40:38', NULL, 'qwen-apikey', 318, '', 'xitong');
INSERT INTO `common_info` VALUES (429, '2026-03-10 09:14:04', NULL, 'qwen-text-model', 319, '', 'xitong');
INSERT INTO `common_info` VALUES (430, '2026-03-18 14:17:21', NULL, '是否开启小程序自动登录', 320, '', 'kaiguan');
INSERT INTO `common_info` VALUES (431, '2026-03-18 14:17:21', NULL, '会员每月查看名片次数', 431, '', 'xitong');

-- ----------------------------
-- Table structure for company_dynamic_record
-- ----------------------------
DROP TABLE IF EXISTS `company_dynamic_record`;
CREATE TABLE `company_dynamic_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `company_card_id` int(0) NULL DEFAULT NULL COMMENT '企业名片id',
  `staff_card_id` int(0) NULL DEFAULT NULL COMMENT '员工名片id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `dynamic_type` int(0) NULL DEFAULT NULL COMMENT '动态类型 1加入 2退出',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '企业动态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_dynamic_record
-- ----------------------------

-- ----------------------------
-- Table structure for company_staff_record
-- ----------------------------
DROP TABLE IF EXISTS `company_staff_record`;
CREATE TABLE `company_staff_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `card_id` int(0) NULL DEFAULT NULL COMMENT '企业名片id',
  `staff_user_id` int(0) NULL DEFAULT NULL COMMENT '企业成员id',
  `staff_card_id` int(0) NULL DEFAULT NULL COMMENT '企业成员名片id',
  `apply_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '申请加入原因',
  `staff_sattus` int(0) NULL DEFAULT NULL COMMENT '成员状态 0申请中 1审核通过 2拒绝 3退出',
  `staff_source` int(0) NULL DEFAULT NULL COMMENT '成员来源 0自主申请 1公司邀请 3公司录入',
  `apply_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `join_time` datetime(0) NULL DEFAULT NULL COMMENT '加入时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `audit_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公司员工信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_staff_record
-- ----------------------------

-- ----------------------------
-- Table structure for company_template
-- ----------------------------
DROP TABLE IF EXISTS `company_template`;
CREATE TABLE `company_template`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `company_card_id` int(0) NULL DEFAULT NULL COMMENT '公司名片id',
  `template_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板名称',
  `back_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '背景图片',
  `company_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司logo图片',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名称',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司名称',
  `companyu_pisition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '职务名称',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `company_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司地址',
  `company_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公司简介',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公司名片模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_template
-- ----------------------------

-- ----------------------------
-- Table structure for company_template_pic
-- ----------------------------
DROP TABLE IF EXISTS `company_template_pic`;
CREATE TABLE `company_template_pic`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_card_id` int(0) NULL DEFAULT NULL COMMENT '公司名片ID',
  `pic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图片地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模板背景图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_template_pic
-- ----------------------------

-- ----------------------------
-- Table structure for help_classify
-- ----------------------------
DROP TABLE IF EXISTS `help_classify`;
CREATE TABLE `help_classify`  (
  `help_classify_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_classify
-- ----------------------------
INSERT INTO `help_classify` VALUES (2, '小程序', 1, 0, '2020-12-12 12:12:12');
INSERT INTO `help_classify` VALUES (3, '活动报名', 2, 0, '2020-12-12 12:12:12');
INSERT INTO `help_classify` VALUES (4, '分类加测试', 0, NULL, '2024-03-19 18:07:41');

-- ----------------------------
-- Table structure for help_word
-- ----------------------------
DROP TABLE IF EXISTS `help_word`;
CREATE TABLE `help_word`  (
  `help_word_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '帮助文档内容',
  `help_classify_id` int(0) NULL DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_word
-- ----------------------------
INSERT INTO `help_word` VALUES (1, '为什么提示不支持打开非业务域名', '因为所以科学道理', 2, 1, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (2, '文件类课间如何保存', '不知道', 2, 2, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (3, '已报名的活动可以取消吗', '可以，需要扣除百分之20', 3, 1, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (6, '规则，请查看', '<p>规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看规则，请查看</p>', 5, 0, '2022-06-07');
INSERT INTO `help_word` VALUES (7, '文章测试', '<p>时代公馆高</p>', 4, 0, '2024-03-19 18:08:34');

-- ----------------------------
-- Table structure for industry
-- ----------------------------
DROP TABLE IF EXISTS `industry`;
CREATE TABLE `industry`  (
  `industry_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '行业id',
  `industry_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '行业名称',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否启用 0不启用 1启用',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '上级id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`industry_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of industry
-- ----------------------------

-- ----------------------------
-- Table structure for industry_news
-- ----------------------------
DROP TABLE IF EXISTS `industry_news`;
CREATE TABLE `industry_news`  (
  `news_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '新闻id',
  `release_type` int(0) NULL DEFAULT NULL COMMENT '发布类型 1用户发布 2平台发布',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `enterprise_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '企业名称',
  `sys_user_id` int(0) NULL DEFAULT NULL COMMENT '平台用户id',
  `sys_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台用户名',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '正文',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态 0待审核 1审核成功 2审核未通过 3会员到期已下架',
  `examine_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '审核备注',
  `title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '封面图',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `industry_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '资讯类型(字典)',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '上下架 0下架 1下架',
  `down_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '下架原因',
  PRIMARY KEY (`news_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '行业资讯' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of industry_news
-- ----------------------------

-- ----------------------------
-- Table structure for industry_news_comment
-- ----------------------------
DROP TABLE IF EXISTS `industry_news_comment`;
CREATE TABLE `industry_news_comment`  (
  `comment_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `news_id` int(0) NULL DEFAULT NULL COMMENT '资讯id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '评论内容',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态 0待审核 1审核通过 2审核不通过',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '动态评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of industry_news_comment
-- ----------------------------

-- ----------------------------
-- Table structure for industry_news_like
-- ----------------------------
DROP TABLE IF EXISTS `industry_news_like`;
CREATE TABLE `industry_news_like`  (
  `like_id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `news_id` int(0) NULL DEFAULT NULL COMMENT '资讯id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`like_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '动态点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of industry_news_like
-- ----------------------------

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(0) NULL DEFAULT NULL COMMENT '被邀请者id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `money_type` int(0) NULL DEFAULT NULL COMMENT '1会员2陪玩',
  `state` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邀请信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite
-- ----------------------------

-- ----------------------------
-- Table structure for invite_money
-- ----------------------------
DROP TABLE IF EXISTS `invite_money`;
CREATE TABLE `invite_money`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '收益钱包id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10, 2) NULL DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_money
-- ----------------------------

-- ----------------------------
-- Table structure for invite_record
-- ----------------------------
DROP TABLE IF EXISTS `invite_record`;
CREATE TABLE `invite_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inviter_id` int(0) NULL DEFAULT NULL COMMENT '邀请人id',
  `inviter_card_id` int(0) NULL DEFAULT NULL COMMENT '邀请人名片id',
  `invitee_id` int(0) NULL DEFAULT NULL COMMENT '被邀请人id',
  `invitee_card_id` int(0) NULL DEFAULT NULL COMMENT '被邀请人名片id',
  `invitee_portrait` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '被邀请人头像',
  `invitee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '被邀请人名称',
  `invite_time` datetime(0) NULL DEFAULT NULL COMMENT '邀请时间',
  `invite_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邀请标题',
  `invite_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邀请描述',
  `invite_type` int(0) NULL DEFAULT NULL COMMENT '邀请类型 0注册 1加入企业',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邀请记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_record
-- ----------------------------

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '排序',
  `vip_id` int(0) NULL DEFAULT NULL COMMENT '会员id',
  `member_describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '会员特权图标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (3, 'https://oss.dzgqb.com/20260304/930229d1f6914d12af9d3c73cd8a62b5.png', '查看访客详情', '2', NULL, '随时查看谁看过你');
INSERT INTO `member` VALUES (4, 'https://oss.dzgqb.com/20260304/7b52980ca26744e698030416d29f7fe3.png', '名片数量不上限', '1', NULL, '创建名片数量不上限');
INSERT INTO `member` VALUES (6, 'https://oss.dzgqb.com/20260304/9388e812baf44e409df375c0b9a03468.png', '查看认证员工', '4', NULL, '随时可看员工信息');
INSERT INTO `member` VALUES (9, 'https://oss.dzgqb.com/20260304/adf67fa11eea4cd8a7dd4008ce1a96d8.png', '增长名片人气值', '5', NULL, '让更多用户看到你');
INSERT INTO `member` VALUES (15, 'https://mpfx.xianmaxiong.com/file/uploadPath/2026/03/11/e5e31a5839c331a683c286617f7bcfd2.png', 'AI核查企业', NULL, NULL, '核查企业真实信息');

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `send_state` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `send_time` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `state` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息',
  `title` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `platform` int(0) NULL DEFAULT NULL COMMENT '来源',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL,
  `by_user_id` int(0) NULL DEFAULT NULL,
  `platform_id` int(0) NULL DEFAULT NULL,
  `demand_id` int(0) NULL DEFAULT NULL COMMENT '采购id',
  `phone` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系方式',
  `classify` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '投诉类型',
  `by_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 510 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_info
-- ----------------------------

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`code`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify`  (
  `pay_classify_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '水晶数量',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------
INSERT INTO `pay_classify` VALUES (1, 0.01, 1.00, 105.00, 1, NULL);
INSERT INTO `pay_classify` VALUES (2, 300.00, 1.00, 320.00, 2, NULL);
INSERT INTO `pay_classify` VALUES (3, 500.00, 1.00, 540.00, 3, NULL);
INSERT INTO `pay_classify` VALUES (4, 1000.00, 1.00, 1100.00, 4, NULL);
INSERT INTO `pay_classify` VALUES (5, 3000.00, 1.00, 3400.00, 5, NULL);
INSERT INTO `pay_classify` VALUES (6, 5000.00, 1.00, 5700.00, 6, NULL);
INSERT INTO `pay_classify` VALUES (7, 10000.00, 1.00, 11500.00, 7, NULL);
INSERT INTO `pay_classify` VALUES (8, 30000.00, 1.00, 35000.00, 8, NULL);

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT ' 1app  2微信二维码支付  3微信小程序 公众号支付 4支付宝APP支付 5支付宝H5支付',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付单号',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `state` int(0) NULL DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付时间',
  `type` int(0) NULL DEFAULT NULL COMMENT '购买类型 1充值点数 2购买会员',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '关联id(如果是购买会员则是会员类型id)',
  `staff_inviter_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '合作商邀请码(开通会员才用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_details
-- ----------------------------

-- ----------------------------
-- Table structure for sc_browse
-- ----------------------------
DROP TABLE IF EXISTS `sc_browse`;
CREATE TABLE `sc_browse`  (
  `browse_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '浏览记录id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `demand_id` int(0) NULL DEFAULT NULL COMMENT '信息id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`browse_id`) USING BTREE,
  INDEX `data_browse`(`demand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_browse
-- ----------------------------

-- ----------------------------
-- Table structure for sc_follow
-- ----------------------------
DROP TABLE IF EXISTS `sc_follow`;
CREATE TABLE `sc_follow`  (
  `follow_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '关注id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `demand_id` int(0) NULL DEFAULT NULL COMMENT '被关注需求id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_follow
-- ----------------------------

-- ----------------------------
-- Table structure for screen_money
-- ----------------------------
DROP TABLE IF EXISTS `screen_money`;
CREATE TABLE `screen_money`  (
  `screen_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '筛选id',
  `min_money` int(0) NULL DEFAULT NULL COMMENT '最小值',
  `max_money` int(0) NULL DEFAULT NULL COMMENT '最大值',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type` int(0) NULL DEFAULT NULL COMMENT '1.价格筛选',
  PRIMARY KEY (`screen_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '范围筛选' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of screen_money
-- ----------------------------
INSERT INTO `screen_money` VALUES (17, 18, 20, 1, '2024-01-04 17:56:47', 1);
INSERT INTO `screen_money` VALUES (18, 20, 30, 2, '2024-01-04 17:56:56', 1);
INSERT INTO `screen_money` VALUES (19, 30, 45, 3, '2024-01-04 17:57:05', 1);
INSERT INTO `screen_money` VALUES (20, 45, 45, 4, '2024-01-04 17:57:21', 1);
INSERT INTO `screen_money` VALUES (21, 160, 170, 1, '2024-01-04 17:57:37', 2);
INSERT INTO `screen_money` VALUES (22, 170, 180, 2, '2024-01-04 17:57:45', 2);
INSERT INTO `screen_money` VALUES (23, 180, 0, 3, '2024-01-04 17:57:52', 2);

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `search_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `key_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关键词',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型 1名片搜索',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 270 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '搜索记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of search
-- ----------------------------

-- ----------------------------
-- Table structure for source_group
-- ----------------------------
DROP TABLE IF EXISTS `source_group`;
CREATE TABLE `source_group`  (
  `group_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '群id',
  `group_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '群名称',
  `group_type` int(0) NULL DEFAULT NULL COMMENT '群分类 1省级群 2市级群',
  `is_enable` int(0) NULL DEFAULT NULL COMMENT '是否启用 0否 1是',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `people_count` int(0) NULL DEFAULT NULL COMMENT '群人数',
  `group_title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '群封面图',
  `group_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '加群二维码',
  `count_money` int(0) NULL DEFAULT NULL COMMENT '加群所需积分数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sys_user_id` int(0) NULL DEFAULT NULL COMMENT '发布人用户id',
  `sys_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发布人昵称',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '群组管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_group
-- ----------------------------

-- ----------------------------
-- Table structure for source_group_role
-- ----------------------------
DROP TABLE IF EXISTS `source_group_role`;
CREATE TABLE `source_group_role`  (
  `role_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `group_id` int(0) NULL DEFAULT NULL COMMENT '群id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `pay_count` int(0) NULL DEFAULT NULL COMMENT '支付点数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '群组用户关联管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_group_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"\",\"qiniuBucketName\":\"\",\"qiniuDomain\":\"\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"\",\"type\":2}', 0, '云存储配置信息');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典值',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `parent_id` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (39, '证书管理', '证书管理', NULL, NULL, 0, NULL, 0);
INSERT INTO `sys_dict` VALUES (40, NULL, NULL, 'IATF 16949', 'IATF 16949', 1, 'IATF 16949', 39);
INSERT INTO `sys_dict` VALUES (41, NULL, NULL, 'ISO 13485', 'ISO 13485', 2, 'ISO 13485', 39);
INSERT INTO `sys_dict` VALUES (42, '岗位', '岗位', NULL, NULL, 2, '岗位', 0);
INSERT INTO `sys_dict` VALUES (43, NULL, NULL, '行政', '行政', 1, '行政', 42);
INSERT INTO `sys_dict` VALUES (44, NULL, NULL, '财务', '财务', 2, '财务', 42);
INSERT INTO `sys_dict` VALUES (45, NULL, NULL, '技术', '技术', 3, '技术', 42);
INSERT INTO `sys_dict` VALUES (46, '渠道', '渠道', NULL, NULL, 0, '渠道', 0);
INSERT INTO `sys_dict` VALUES (47, NULL, NULL, '抖音', '抖音', 1, '抖音', 46);
INSERT INTO `sys_dict` VALUES (48, NULL, NULL, '1688', '1688', 2, '1688', 46);
INSERT INTO `sys_dict` VALUES (49, NULL, NULL, 'B2B平台', 'B2B平台', 3, 'B2B平台', 46);
INSERT INTO `sys_dict` VALUES (50, NULL, NULL, '其他', '其他', 4, '其他', 46);
INSERT INTO `sys_dict` VALUES (51, '举报类型', '举报类型', NULL, NULL, 0, NULL, 0);
INSERT INTO `sys_dict` VALUES (52, NULL, NULL, '虚假信息', '虚假信息', 1, '虚假信息', 51);
INSERT INTO `sys_dict` VALUES (53, NULL, NULL, '与事实不符', '与事实不符', 2, '与事实不符', 51);
INSERT INTO `sys_dict` VALUES (54, '资讯类型', '资讯类型', NULL, NULL, 0, NULL, 0);
INSERT INTO `sys_dict` VALUES (55, NULL, NULL, '资讯', '资讯', 0, '资讯', NULL);
INSERT INTO `sys_dict` VALUES (56, NULL, NULL, '招聘', '招聘', 0, '招聘', NULL);
INSERT INTO `sys_dict` VALUES (57, NULL, NULL, '咨询', '咨询', 0, '咨询', 54);
INSERT INTO `sys_dict` VALUES (58, NULL, NULL, '供应', '供应', 0, '供应', 54);
INSERT INTO `sys_dict` VALUES (59, '资质证书', '资质证书', NULL, NULL, 0, NULL, 0);
INSERT INTO `sys_dict` VALUES (60, NULL, NULL, '系统分析师', '系统分析师', 0, NULL, 59);
INSERT INTO `sys_dict` VALUES (61, NULL, NULL, '系统架构设计师', '系统架构设计师', 0, NULL, 59);
INSERT INTO `sys_dict` VALUES (62, NULL, NULL, '\'网络工程师', '\'网络工程师', 0, NULL, 59);
INSERT INTO `sys_dict` VALUES (63, NULL, NULL, '人力资源管理师', '人力资源管理师', 0, NULL, 59);
INSERT INTO `sys_dict` VALUES (64, NULL, NULL, '心理咨询师', '心理咨询师', 0, NULL, 59);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(0) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 583 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 231 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, 126, '管理员列表', 'sys/user', NULL, 1, 'admin', 10);
INSERT INTO `sys_menu` VALUES (3, 126, '角色管理', 'sys/role', NULL, 1, 'role', 11);
INSERT INTO `sys_menu` VALUES (4, 126, '菜单管理', 'sys/menu', NULL, 1, 'menu', 12);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 0, '用户中心', 'userList', '', 1, 'yonghul', 1);
INSERT INTO `sys_menu` VALUES (33, 0, '数据中心', 'home', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` VALUES (34, 0, '财务中心', 'financeList', '', 1, 'caiwu', 1);
INSERT INTO `sys_menu` VALUES (35, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (36, 34, '转账', '', 'financeList:transfer', 2, '', 0);
INSERT INTO `sys_menu` VALUES (37, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (38, 34, '退款', '', 'financeList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (39, 34, '修改', '', 'financeList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (40, 34, '删除', '', 'financeList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (41, 0, '消息中心', 'message', '', 1, 'xiaoxi', 1);
INSERT INTO `sys_menu` VALUES (42, 41, '查看', '', 'message:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (43, 41, '消息推送', '', 'message:push', 2, '', 0);
INSERT INTO `sys_menu` VALUES (50, 0, '首页装修', 'bannerList', '', 1, 'shangpin', 2);
INSERT INTO `sys_menu` VALUES (51, 50, '查看', '', 'bannerList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (52, 50, '添加', '', 'bannerList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (53, 50, '修改', '', 'bannerList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (54, 50, '删除', '', 'bannerList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (57, 0, '系统配置', 'allocationList', '', 1, 'menu', 19);
INSERT INTO `sys_menu` VALUES (58, 57, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (59, 57, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (60, 32, '查看', '', 'userList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (61, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (69, 41, '添加', '', 'message:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (70, 41, '修改', '', 'message:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (71, 41, '删除', '', 'message:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (72, 0, '样式管理', 'locality', '', 1, 'order', 5);
INSERT INTO `sys_menu` VALUES (73, 72, '添加', '', 'locality:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (74, 72, '查看', '', 'locality:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (75, 72, '修改', '', 'locality:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (76, 72, '删除', '', 'locality:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (90, 32, '修改积分', '', 'userList:updatejf', 2, '', 0);
INSERT INTO `sys_menu` VALUES (91, 32, '修改用户状态', '', 'userList:updateStatus', 2, '', 0);
INSERT INTO `sys_menu` VALUES (105, 32, '修改信息', '', 'userList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (116, 0, '会员配置', 'coupon', '', 1, 'caiwu', 7);
INSERT INTO `sys_menu` VALUES (117, 116, '查看', '', 'coupon:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (118, 116, '添加', '', 'coupon:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (119, 116, '修改', '', 'coupon:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (120, 116, '删除', '', 'coupon:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (126, 0, '系统管理', '', '', 0, 'shezhi', 20);
INSERT INTO `sys_menu` VALUES (144, 0, '帮助中心', 'materialsList', '', 1, 'renwu', 8);
INSERT INTO `sys_menu` VALUES (145, 144, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (146, 144, '添加', '', 'materialsList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (147, 144, '修改', '', 'materialsList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (148, 144, '删除', '', 'materialsList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (161, 32, '设置会员', '', 'userList:updateVip', 2, '', 0);
INSERT INTO `sys_menu` VALUES (162, 0, '注销信息', 'messageZx', NULL, 1, 'order', 1);
INSERT INTO `sys_menu` VALUES (163, 162, '查看', NULL, 'messageZx:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (164, 162, '注销', NULL, 'messageZx:shenhe', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (173, 34, '导出', NULL, 'financeList:daochu', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (174, 0, '会员特权', 'vipPrivilege', NULL, 1, 'shoucangfill', 7);
INSERT INTO `sys_menu` VALUES (175, 174, '查看', NULL, 'vipPrivilege:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (176, 174, '添加', NULL, 'vipPrivilege:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (177, 174, '修改', NULL, 'vipPrivilege:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (178, 174, '删除', NULL, 'vipPrivilege:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (189, 0, '名片管理', 'businessCard', NULL, 1, 'editor', 4);
INSERT INTO `sys_menu` VALUES (190, 189, '查看', NULL, 'businessCard:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (191, 189, '添加', NULL, 'businessCard:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (192, 189, '修改', NULL, 'businessCard:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (193, 189, '删除', NULL, 'businessCard:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (194, 0, '模板管理', 'crowd', NULL, 1, 'fenleilist', 4);
INSERT INTO `sys_menu` VALUES (195, 194, '查看', NULL, 'crowd:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (196, 194, '添加', NULL, 'crowd:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (197, 194, '修改', NULL, 'crowd:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (198, 194, '删除', NULL, 'crowd:delet', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (214, 126, '字典', 'sys/dict', NULL, 1, 'fenleilist', 0);
INSERT INTO `sys_menu` VALUES (215, 214, '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (216, 214, '添加', NULL, 'sys:dict:save', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (217, 214, '修改', NULL, 'sys:dict:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (218, 214, '删除', NULL, 'sys:dict:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (219, 189, '审核', NULL, 'businessCard:shenhe', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (221, 0, '名片排行榜', 'riderTop', NULL, 1, 'tubiao', 6);
INSERT INTO `sys_menu` VALUES (222, 221, '查看', NULL, 'riderTop:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (226, 0, '行业分类', 'information', NULL, 1, 'menu', 5);
INSERT INTO `sys_menu` VALUES (227, 226, '查看', NULL, 'information:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (228, 226, '添加', NULL, 'information:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (229, 226, '修改', NULL, 'information:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (230, 226, '删除', NULL, 'information:delete', 2, NULL, 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'super', 2, '2021-06-04 17:38:28');
INSERT INTO `sys_role` VALUES (4, '普通用户', '普通用户', 2, '2021-09-10 15:33:47');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7592 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (5295, 4, 33);
INSERT INTO `sys_role_menu` VALUES (5296, 4, 60);
INSERT INTO `sys_role_menu` VALUES (5297, 4, 90);
INSERT INTO `sys_role_menu` VALUES (5298, 4, 91);
INSERT INTO `sys_role_menu` VALUES (5299, 4, 105);
INSERT INTO `sys_role_menu` VALUES (5300, 4, 161);
INSERT INTO `sys_role_menu` VALUES (5301, 4, 35);
INSERT INTO `sys_role_menu` VALUES (5302, 4, 36);
INSERT INTO `sys_role_menu` VALUES (5303, 4, 37);
INSERT INTO `sys_role_menu` VALUES (5304, 4, 38);
INSERT INTO `sys_role_menu` VALUES (5305, 4, 39);
INSERT INTO `sys_role_menu` VALUES (5306, 4, 173);
INSERT INTO `sys_role_menu` VALUES (5307, 4, 42);
INSERT INTO `sys_role_menu` VALUES (5308, 4, 162);
INSERT INTO `sys_role_menu` VALUES (5309, 4, 163);
INSERT INTO `sys_role_menu` VALUES (5310, 4, 164);
INSERT INTO `sys_role_menu` VALUES (5311, 4, 51);
INSERT INTO `sys_role_menu` VALUES (5319, 4, 73);
INSERT INTO `sys_role_menu` VALUES (5320, 4, 74);
INSERT INTO `sys_role_menu` VALUES (5321, 4, 75);
INSERT INTO `sys_role_menu` VALUES (5333, 4, 117);
INSERT INTO `sys_role_menu` VALUES (5334, 4, 118);
INSERT INTO `sys_role_menu` VALUES (5335, 4, 119);
INSERT INTO `sys_role_menu` VALUES (5336, 4, 144);
INSERT INTO `sys_role_menu` VALUES (5337, 4, 145);
INSERT INTO `sys_role_menu` VALUES (5338, 4, 146);
INSERT INTO `sys_role_menu` VALUES (5339, 4, 147);
INSERT INTO `sys_role_menu` VALUES (5340, 4, 148);
INSERT INTO `sys_role_menu` VALUES (5346, 4, 58);
INSERT INTO `sys_role_menu` VALUES (5348, 4, 15);
INSERT INTO `sys_role_menu` VALUES (5349, 4, 19);
INSERT INTO `sys_role_menu` VALUES (5350, 4, 23);
INSERT INTO `sys_role_menu` VALUES (5351, 4, -666666);
INSERT INTO `sys_role_menu` VALUES (5352, 4, 32);
INSERT INTO `sys_role_menu` VALUES (5353, 4, 34);
INSERT INTO `sys_role_menu` VALUES (5354, 4, 41);
INSERT INTO `sys_role_menu` VALUES (5355, 4, 50);
INSERT INTO `sys_role_menu` VALUES (5356, 4, 72);
INSERT INTO `sys_role_menu` VALUES (5359, 4, 116);
INSERT INTO `sys_role_menu` VALUES (5360, 4, 57);
INSERT INTO `sys_role_menu` VALUES (5361, 4, 126);
INSERT INTO `sys_role_menu` VALUES (5363, 4, 2);
INSERT INTO `sys_role_menu` VALUES (5364, 4, 3);
INSERT INTO `sys_role_menu` VALUES (5365, 4, 4);
INSERT INTO `sys_role_menu` VALUES (7499, 1, 33);
INSERT INTO `sys_role_menu` VALUES (7500, 1, 32);
INSERT INTO `sys_role_menu` VALUES (7501, 1, 60);
INSERT INTO `sys_role_menu` VALUES (7502, 1, 61);
INSERT INTO `sys_role_menu` VALUES (7503, 1, 90);
INSERT INTO `sys_role_menu` VALUES (7504, 1, 91);
INSERT INTO `sys_role_menu` VALUES (7505, 1, 105);
INSERT INTO `sys_role_menu` VALUES (7506, 1, 161);
INSERT INTO `sys_role_menu` VALUES (7507, 1, 34);
INSERT INTO `sys_role_menu` VALUES (7508, 1, 35);
INSERT INTO `sys_role_menu` VALUES (7509, 1, 36);
INSERT INTO `sys_role_menu` VALUES (7510, 1, 37);
INSERT INTO `sys_role_menu` VALUES (7511, 1, 38);
INSERT INTO `sys_role_menu` VALUES (7512, 1, 39);
INSERT INTO `sys_role_menu` VALUES (7513, 1, 40);
INSERT INTO `sys_role_menu` VALUES (7514, 1, 173);
INSERT INTO `sys_role_menu` VALUES (7515, 1, 41);
INSERT INTO `sys_role_menu` VALUES (7516, 1, 42);
INSERT INTO `sys_role_menu` VALUES (7517, 1, 43);
INSERT INTO `sys_role_menu` VALUES (7518, 1, 69);
INSERT INTO `sys_role_menu` VALUES (7519, 1, 70);
INSERT INTO `sys_role_menu` VALUES (7520, 1, 71);
INSERT INTO `sys_role_menu` VALUES (7521, 1, 162);
INSERT INTO `sys_role_menu` VALUES (7522, 1, 163);
INSERT INTO `sys_role_menu` VALUES (7523, 1, 164);
INSERT INTO `sys_role_menu` VALUES (7524, 1, 50);
INSERT INTO `sys_role_menu` VALUES (7525, 1, 51);
INSERT INTO `sys_role_menu` VALUES (7526, 1, 52);
INSERT INTO `sys_role_menu` VALUES (7527, 1, 53);
INSERT INTO `sys_role_menu` VALUES (7528, 1, 54);
INSERT INTO `sys_role_menu` VALUES (7529, 1, 189);
INSERT INTO `sys_role_menu` VALUES (7530, 1, 190);
INSERT INTO `sys_role_menu` VALUES (7531, 1, 191);
INSERT INTO `sys_role_menu` VALUES (7532, 1, 192);
INSERT INTO `sys_role_menu` VALUES (7533, 1, 193);
INSERT INTO `sys_role_menu` VALUES (7534, 1, 219);
INSERT INTO `sys_role_menu` VALUES (7535, 1, 194);
INSERT INTO `sys_role_menu` VALUES (7536, 1, 195);
INSERT INTO `sys_role_menu` VALUES (7537, 1, 196);
INSERT INTO `sys_role_menu` VALUES (7538, 1, 197);
INSERT INTO `sys_role_menu` VALUES (7539, 1, 198);
INSERT INTO `sys_role_menu` VALUES (7540, 1, 72);
INSERT INTO `sys_role_menu` VALUES (7541, 1, 73);
INSERT INTO `sys_role_menu` VALUES (7542, 1, 74);
INSERT INTO `sys_role_menu` VALUES (7543, 1, 75);
INSERT INTO `sys_role_menu` VALUES (7544, 1, 76);
INSERT INTO `sys_role_menu` VALUES (7545, 1, 226);
INSERT INTO `sys_role_menu` VALUES (7546, 1, 227);
INSERT INTO `sys_role_menu` VALUES (7547, 1, 228);
INSERT INTO `sys_role_menu` VALUES (7548, 1, 229);
INSERT INTO `sys_role_menu` VALUES (7549, 1, 230);
INSERT INTO `sys_role_menu` VALUES (7550, 1, 221);
INSERT INTO `sys_role_menu` VALUES (7551, 1, 222);
INSERT INTO `sys_role_menu` VALUES (7552, 1, 116);
INSERT INTO `sys_role_menu` VALUES (7553, 1, 117);
INSERT INTO `sys_role_menu` VALUES (7554, 1, 118);
INSERT INTO `sys_role_menu` VALUES (7555, 1, 119);
INSERT INTO `sys_role_menu` VALUES (7556, 1, 120);
INSERT INTO `sys_role_menu` VALUES (7557, 1, 174);
INSERT INTO `sys_role_menu` VALUES (7558, 1, 175);
INSERT INTO `sys_role_menu` VALUES (7559, 1, 176);
INSERT INTO `sys_role_menu` VALUES (7560, 1, 177);
INSERT INTO `sys_role_menu` VALUES (7561, 1, 178);
INSERT INTO `sys_role_menu` VALUES (7562, 1, 144);
INSERT INTO `sys_role_menu` VALUES (7563, 1, 145);
INSERT INTO `sys_role_menu` VALUES (7564, 1, 146);
INSERT INTO `sys_role_menu` VALUES (7565, 1, 147);
INSERT INTO `sys_role_menu` VALUES (7566, 1, 148);
INSERT INTO `sys_role_menu` VALUES (7567, 1, 57);
INSERT INTO `sys_role_menu` VALUES (7568, 1, 58);
INSERT INTO `sys_role_menu` VALUES (7569, 1, 59);
INSERT INTO `sys_role_menu` VALUES (7570, 1, 126);
INSERT INTO `sys_role_menu` VALUES (7571, 1, 214);
INSERT INTO `sys_role_menu` VALUES (7572, 1, 215);
INSERT INTO `sys_role_menu` VALUES (7573, 1, 216);
INSERT INTO `sys_role_menu` VALUES (7574, 1, 217);
INSERT INTO `sys_role_menu` VALUES (7575, 1, 218);
INSERT INTO `sys_role_menu` VALUES (7576, 1, 2);
INSERT INTO `sys_role_menu` VALUES (7577, 1, 15);
INSERT INTO `sys_role_menu` VALUES (7578, 1, 16);
INSERT INTO `sys_role_menu` VALUES (7579, 1, 17);
INSERT INTO `sys_role_menu` VALUES (7580, 1, 18);
INSERT INTO `sys_role_menu` VALUES (7581, 1, 3);
INSERT INTO `sys_role_menu` VALUES (7582, 1, 19);
INSERT INTO `sys_role_menu` VALUES (7583, 1, 20);
INSERT INTO `sys_role_menu` VALUES (7584, 1, 21);
INSERT INTO `sys_role_menu` VALUES (7585, 1, 22);
INSERT INTO `sys_role_menu` VALUES (7586, 1, 4);
INSERT INTO `sys_role_menu` VALUES (7587, 1, 23);
INSERT INTO `sys_role_menu` VALUES (7588, 1, 24);
INSERT INTO `sys_role_menu` VALUES (7589, 1, 25);
INSERT INTO `sys_role_menu` VALUES (7590, 1, 26);
INSERT INTO `sys_role_menu` VALUES (7591, 1, -666666);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `inviter_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'b1672b04628a52e4a3d7a472c691564c7b9d92e3ee38abd96f78bab92446b9a2', 'gOQpg687tI4FgraTuw2v', 'root@163.com', '13612345678', 1, 10, '2016-11-11 11:11:11', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 1, 1);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(0) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  `sex` int(0) NULL DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(0) NULL DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信公众号openId',
  `account_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信App  openId',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `count_money` int(0) NULL DEFAULT 0 COMMENT '点数',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '加密盐',
  `apple_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(0) NULL DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(0) NULL DEFAULT 1 COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `clientid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'app推送id',
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付宝账号',
  `is_send_msg` int(0) NULL DEFAULT NULL COMMENT '是否推送消息',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信提现图片',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '最近一条信息内容',
  `user_type` int(0) NULL DEFAULT 0 COMMENT '用户类型 0未认证 1普通用户 2企业用户',
  `is_vip` int(0) NULL DEFAULT 0 COMMENT '是否是会员(0不是 1是)',
  `vip_end_time` datetime(0) NULL DEFAULT NULL COMMENT 'vip到期时间',
  `vip_id` int(0) NULL DEFAULT NULL COMMENT 'vipId',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `invitation_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '代理人邀请码',
  `inviter_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '代理人上级邀请码',
  `staff_invitation_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '合作商邀请码',
  `staff_inviter_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '合作商上级邀请码',
  `is_agent` int(0) NULL DEFAULT 0 COMMENT '是否是代理 0否 1是',
  `is_staff` int(0) NULL DEFAULT 0 COMMENT '是否是合伙人 0否 1是',
  `is_promoter` int(0) NULL DEFAULT 0 COMMENT '是否推广员 0=否 1=是',
  `vip_card_num` int(0) NULL DEFAULT NULL COMMENT '会员申请名片次数',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 249 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '官方', '13000000000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '', '', NULL, NULL, 1, 'H5', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 15, '2025-11-10 14:54:26', '2026-01-12 13:27:50', '666666', '666666', '666666', '666666', 0, 0, 0, 0);

-- ----------------------------
-- Table structure for user_card_groups
-- ----------------------------
DROP TABLE IF EXISTS `user_card_groups`;
CREATE TABLE `user_card_groups`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ' ',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `groups_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分组名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sort_id` int(0) NULL DEFAULT NULL COMMENT '排序序号',
  `is_default` tinyint(1) NULL DEFAULT 2 COMMENT '是否默认 1=默认 2=不默认',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户名片分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_card_groups
-- ----------------------------

-- ----------------------------
-- Table structure for user_card_list
-- ----------------------------
DROP TABLE IF EXISTS `user_card_list`;
CREATE TABLE `user_card_list`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `card_id` int(0) NULL DEFAULT NULL COMMENT '名片id',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分组id',
  `card_remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名片备注',
  `card_spell` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '拼音',
  `card_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名片头像',
  `real_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '真实姓名',
  `card_position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '职务名称',
  `card_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司名称',
  `collect_time` datetime(0) NULL DEFAULT NULL COMMENT '收藏/添加时间',
  `contact_count` int(0) NULL DEFAULT 0 COMMENT '联系次数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户名片夹' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_card_list
-- ----------------------------

-- ----------------------------
-- Table structure for user_certification
-- ----------------------------
DROP TABLE IF EXISTS `user_certification`;
CREATE TABLE `user_certification`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户实名认证id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '认证类型  1普通用户 2企业用户',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '真实姓名|法人姓名',
  `identity_card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '证件号码|法人身份证',
  `enterprise_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '企业名称',
  `enterprise_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '企业联系电话',
  `business_license` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '营业执照',
  `business_license_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '营业执照图片',
  `status` int(0) NULL DEFAULT NULL COMMENT '0待审核 1已通过 2未通过',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审核原因',
  `identity_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证正面图片',
  `identity_card_verso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证反面图片',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `enterprise_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '个人详细地址|企业地址',
  `is_submit` int(0) NULL DEFAULT 0 COMMENT '是否允许提交0是 1否',
  `capital` decimal(10, 2) NULL DEFAULT NULL COMMENT '企业注册资金',
  `registration_date` date NULL DEFAULT NULL COMMENT '企业注册日期',
  `business_hours` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '企业营业时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_certification
-- ----------------------------

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`  (
  `follow_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `follow_user_id` bigint(0) NULL DEFAULT NULL COMMENT '关注的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '钱包金额',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 550 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money
-- ----------------------------

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(0) NULL DEFAULT NULL COMMENT '邀请用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `classify` int(0) NULL DEFAULT NULL COMMENT '用途 1购买vip 2充值点数 3发布采购 4用户支付点数 5下级分成',
  `money_type` int(0) NULL DEFAULT NULL COMMENT '钱包类型 1积分 2钱包',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型 1增加 2减少',
  `state` int(0) NULL DEFAULT 1 COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_level` int(0) NULL DEFAULT NULL COMMENT '1一级 2二级',
  `relation_id` int(0) NULL DEFAULT NULL COMMENT '关联id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1576 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for user_vip_staff
-- ----------------------------
DROP TABLE IF EXISTS `user_vip_staff`;
CREATE TABLE `user_vip_staff`  (
  `record_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `staff_id` int(0) NULL DEFAULT NULL COMMENT '员工id',
  `vip_id` int(0) NULL DEFAULT NULL COMMENT '会员id',
  `pay_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户会员关联员工' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_vip_staff
-- ----------------------------

-- ----------------------------
-- Table structure for verify_record
-- ----------------------------
DROP TABLE IF EXISTS `verify_record`;
CREATE TABLE `verify_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司名',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '核查结果',
  `verify_time` datetime(0) NULL DEFAULT NULL COMMENT '核查时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '核查记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of verify_record
-- ----------------------------

-- ----------------------------
-- Table structure for vip_details
-- ----------------------------
DROP TABLE IF EXISTS `vip_details`;
CREATE TABLE `vip_details`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_month_count` int(0) NULL DEFAULT NULL COMMENT '月数',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员价格',
  `vip_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '会员名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_enable` int(0) NULL DEFAULT 0 COMMENT '是否启用(0未启用 1启用)',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '会员配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_details
-- ----------------------------
INSERT INTO `vip_details` VALUES (13, 12, 199.00, '年会员', NULL, '2026-03-17 17:21:40', 1, 3);
INSERT INTO `vip_details` VALUES (14, 1, 9.90, '月会员', '2025-11-21 14:05:43', '2026-03-17 17:21:49', 1, 1);
INSERT INTO `vip_details` VALUES (15, 3, 59.90, '季度会员', '2025-11-24 13:35:28', '2025-11-24 13:36:09', 1, 2);

-- ----------------------------
-- Table structure for visit_card_record
-- ----------------------------
DROP TABLE IF EXISTS `visit_card_record`;
CREATE TABLE `visit_card_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `card_id` int(0) NULL DEFAULT NULL COMMENT '名片id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名称',
  `visit_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '访问来源',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '访问记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of visit_card_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
