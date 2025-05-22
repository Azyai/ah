/*
 Navicat Premium Data Transfer

 Source Server         : AYz
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : ah2025

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 22/05/2025 22:25:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `type` tinyint NOT NULL COMMENT '1大转盘 2福袋 3立即开奖',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '1未开始 2进行中 3已结束',
  `rule_config` json NOT NULL COMMENT '抽奖规则配置',
  `max_participants` int UNSIGNED NULL DEFAULT NULL COMMENT '最大参与人数限制',
  `current_participants` int UNSIGNED NULL DEFAULT 0 COMMENT '当前参与人数',
  `auto_close` tinyint NULL DEFAULT 0 COMMENT '是否自动关闭',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_time_status`(`start_time` ASC, `end_time` ASC, `status` ASC) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (1, '幸运大转盘活动 - 已更新', 1, '2025-05-01 00:00:00', '2025-07-31 23:59:59', 2, '{\"prizeList\": [1, 2, 4], \"allowShare\": false, \"shareReward\": 0, \"backgroundImg\": \"http://example.com/images/lucky_wheel_updated.png\", \"animationSpeed\": 7}', 1500, 247, 0, 1, '2025-05-16 08:43:28', '2025-05-22 22:21:17');
INSERT INTO `activity` VALUES (2, '新用户注册活动', 1, '2025-10-01 00:00:00', '2025-10-31 23:59:59', 2, '{\"user_filter\": {\"first_login\": false, \"register_days\": 30}, \"min_prize_id\": 5, \"prize_guarantee\": true}', 50000, 0, 0, 0, '2025-05-16 08:43:28', '2025-05-19 09:22:13');
INSERT INTO `activity` VALUES (3, '老用户回归专属礼包', 3, '2025-10-01 00:00:00', '2025-10-31 23:59:59', 2, '{\"user_filter\": {\"first_login\": false, \"register_days\": 30}, \"min_prize_id\": 5, \"prize_guarantee\": true}', 50000, 0, 0, 0, '2025-05-16 08:45:27', '2025-05-17 09:56:52');
INSERT INTO `activity` VALUES (4, '2025端午福袋', 2, '2025-06-10 00:00:00', '2025-06-12 23:59:59', 2, '{\"open_time\": \"2025-06-12 20:00:00\", \"prize_list\": [11, 12, 13, 14, 15], \"daily_limit\": 3, \"background_img\": \"https://example.com/dragon-boat.jpg\"}', 50000, 0, 1, 0, '2025-05-17 09:44:10', '2025-05-17 09:57:40');
INSERT INTO `activity` VALUES (5, '新用户注册礼', 3, '2025-08-01 00:00:00', '2025-08-31 23:59:59', 1, '{\"prize_id\": 16, \"user_type\": \"new\", \"probability\": 100, \"background_img\": \"https://example.com/welcome.jpg\"}', NULL, 0, 0, 0, '2025-05-17 09:44:10', '2025-05-17 09:57:40');
INSERT INTO `activity` VALUES (6, '春节大转盘', 1, '2025-01-20 00:00:00', '2025-02-05 23:59:59', 3, '{\"prize_list\": [1, 2, 3, 4, 5], \"allow_share\": true, \"share_reward\": 1, \"background_img\": \"https://example.com/spring-festival.jpg\", \"animation_speed\": 3}', 200000, 187642, 1, 1, '2025-05-17 09:44:10', '2025-05-17 09:44:10');
INSERT INTO `activity` VALUES (7, '测试活动-已删除', 2, '2025-03-01 00:00:00', '2025-03-02 23:59:59', 3, '{\"open_time\": \"2025-03-02 12:00:00\", \"prize_list\": [99, 100], \"background_img\": \"https://example.com/test.jpg\"}', 1000, 23, 0, 1, '2025-05-17 09:44:10', '2025-05-17 09:44:32');
INSERT INTO `activity` VALUES (8, '周年庆无限转盘', 1, '2025-09-15 00:00:00', '2025-09-30 23:59:59', 1, '{\"prize_list\": [20, 21, 22, 23, 24, 25], \"allow_share\": true, \"share_reward\": 2, \"background_img\": \"https://example.com/anniversary.jpg\", \"animation_speed\": 2.5}', NULL, 0, 1, 1, '2025-05-17 09:44:11', '2025-05-17 09:44:11');
INSERT INTO `activity` VALUES (9, '双十一幸运大转盘', 1, '2025-11-11 00:00:00', '2025-11-12 23:59:59', 1, '{\"allowShare\": true, \"shareReward\": 1, \"draw_type_config\": {\"prizeList\": [1, 2, 3, 4, 5], \"backgroundImg\": \"https://example.com/lucky-wheel.jpg\", \"animationSpeed\": 3}}', 50000, 0, 1, 1, '2025-05-17 11:41:04', '2025-05-17 11:41:04');
INSERT INTO `activity` VALUES (10, '幸运大转盘活动333', 1, '2025-06-01 00:00:00', '2025-06-30 23:59:59', 1, '{\"prizeList\": [1, 2, 3], \"allowShare\": true, \"shareReward\": 1, \"backgroundImg\": \"http://example.com/images/lucky_wheel.png\", \"animationSpeed\": 5}', 1000, 0, 0, 1, '2025-05-19 09:27:03', '2025-05-19 09:27:03');

-- ----------------------------
-- Table structure for activity_prize
-- ----------------------------
DROP TABLE IF EXISTS `activity_prize`;
CREATE TABLE `activity_prize`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `activity_id` int UNSIGNED NOT NULL,
  `prize_id` int UNSIGNED NOT NULL,
  `total_stock` int NOT NULL COMMENT '总库存',
  `used_stock` int NOT NULL DEFAULT 0 COMMENT '已用库存',
  `probability` decimal(5, 4) NOT NULL COMMENT '中奖概率',
  `show_order` int NULL DEFAULT NULL COMMENT '展示顺序',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_activity`(`activity_id` ASC) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE,
  INDEX `prize_id`(`prize_id` ASC) USING BTREE,
  CONSTRAINT `activity_prize_ibfk_1` FOREIGN KEY (`prize_id`) REFERENCES `prize` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_activity_prize` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_prize
-- ----------------------------
INSERT INTO `activity_prize` VALUES (1, 1, 1, 200, 1, 0.0500, 1, 1, '2025-05-17 11:41:04', '2025-05-21 23:37:25');
INSERT INTO `activity_prize` VALUES (2, 1, 2, 300, 8, 0.1500, 2, 1, '2025-05-17 11:41:04', '2025-05-22 22:21:17');
INSERT INTO `activity_prize` VALUES (3, 9, 3, 200, 0, 0.2000, 3, 1, '2025-05-17 11:41:04', '2025-05-17 11:41:04');
INSERT INTO `activity_prize` VALUES (4, 10, 1, 100, 0, 0.1000, NULL, 1, '2025-05-19 09:27:03', '2025-05-19 09:27:03');
INSERT INTO `activity_prize` VALUES (5, 10, 2, 200, 0, 0.3000, NULL, 1, '2025-05-19 09:27:03', '2025-05-19 09:27:03');
INSERT INTO `activity_prize` VALUES (6, 10, 3, 700, 0, 0.6000, NULL, 1, '2025-05-19 09:27:03', '2025-05-19 09:27:03');

-- ----------------------------
-- Table structure for activity_restriction
-- ----------------------------
DROP TABLE IF EXISTS `activity_restriction`;
CREATE TABLE `activity_restriction`  (
  `activity_id` int UNSIGNED NOT NULL,
  `limit_type` tinyint NOT NULL COMMENT '1总次数 2每日次数 3频率限制',
  `limit_value` int NOT NULL COMMENT '限制值',
  `interval_seconds` int NULL DEFAULT NULL COMMENT '间隔秒数',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE,
  CONSTRAINT `activity_restriction_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_restriction
-- ----------------------------
INSERT INTO `activity_restriction` VALUES (1, 2, 3, 3600, 1, '2025-05-19 09:32:11', '2025-05-18 19:41:30');
INSERT INTO `activity_restriction` VALUES (10, 1, 5, 86400, 1, '2025-05-19 09:27:03', '2025-05-19 09:27:03');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `state` int NULL DEFAULT NULL COMMENT '节点类型',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单url',
  `p_id` int NULL DEFAULT NULL COMMENT '上级菜单id',
  `acl_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `grade` int NULL DEFAULT NULL COMMENT '菜单层级',
  `is_del` int NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 'menu-1', '总系统(后台登录)', 1, '/', -1, '2099', -1, 0);
INSERT INTO `menu` VALUES (2, 'menu-2', '用户系统cloud_user', 0, '/user_system', 1, '10', 0, 0);
INSERT INTO `menu` VALUES (3, 'menu-3', '用户管理', 0, '/user', 2, '1010', 1, 0);
INSERT INTO `menu` VALUES (4, 'menu-4', '角色管理', 0, '/role', 2, '1020', 1, 0);
INSERT INTO `menu` VALUES (5, 'menu-5', '菜单管理', 0, '/menu', 2, '1030', 1, 0);
INSERT INTO `menu` VALUES (6, 'menu-6', '增加用户信息', 0, '/user/add', 3, '101001', 2, 0);
INSERT INTO `menu` VALUES (7, NULL, '删除用户信息', 0, '/user/delete', 3, '101002', 2, 0);
INSERT INTO `menu` VALUES (8, NULL, '修改用户信息', 0, '/user/update', 3, '101003', 2, 0);
INSERT INTO `menu` VALUES (9, NULL, '查询用户信息', 0, '/user/select', 3, '101004', 2, 0);
INSERT INTO `menu` VALUES (10, NULL, '抽奖系统cloud_prize_consumer', 0, '/prize_system', 1, '20', 0, 0);
INSERT INTO `menu` VALUES (11, NULL, '测试用', 0, '/test/auth', 10, '2010', 1, 0);

-- ----------------------------
-- Table structure for participation
-- ----------------------------
DROP TABLE IF EXISTS `participation`;
CREATE TABLE `participation`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` bigint UNSIGNED NOT NULL,
  `activity_id` int UNSIGNED NOT NULL,
  `participate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `device_fingerprint` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备指纹',
  `is_winning` tinyint NULL DEFAULT 0 COMMENT '是否中奖',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_activity`(`user_id` ASC, `activity_id` ASC) USING BTREE,
  INDEX `idx_activity`(`activity_id` ASC) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE,
  CONSTRAINT `participation_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of participation
-- ----------------------------
INSERT INTO `participation` VALUES ('272d4920-905d-4cbd-9e45-c7791eee09bf', 2, 1, '2025-05-22 22:21:10', '192.168.14.1', 'xxxxx', 0, 1, '2025-05-22 22:21:10', '2025-05-22 22:21:10');
INSERT INTO `participation` VALUES ('58aa01fd-6428-4e34-85fe-8c38ac434883', 2, 1, '2025-05-22 22:21:15', '192.168.14.1', 'xxxxx', 0, 1, '2025-05-22 22:21:15', '2025-05-22 22:21:15');
INSERT INTO `participation` VALUES ('65728316-a204-4feb-85ff-c8a3740c6419', 2, 1, '2025-05-22 22:21:14', '192.168.14.1', 'xxxxx', 0, 1, '2025-05-22 22:21:14', '2025-05-22 22:21:14');
INSERT INTO `participation` VALUES ('7232c04f-66af-412e-825b-10956810361f', 2, 1, '2025-05-22 22:21:11', '192.168.14.1', 'xxxxx', 0, 1, '2025-05-22 22:21:11', '2025-05-22 22:21:11');
INSERT INTO `participation` VALUES ('7f5f4e57-5d76-4800-996f-742013e77142', 2, 1, '2025-05-22 22:21:12', '192.168.14.1', 'xxxxx', 0, 1, '2025-05-22 22:21:12', '2025-05-22 22:21:12');
INSERT INTO `participation` VALUES ('dac2eed1-eb6b-4fc6-8e81-858c8959f33c', 2, 1, '2025-05-22 22:21:17', '192.168.14.1', 'xxxxx', 1, 1, '2025-05-22 22:21:17', '2025-05-22 22:21:17');

-- ----------------------------
-- Table structure for prize
-- ----------------------------
DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品名称',
  `is_virtual` tinyint NOT NULL DEFAULT 0 COMMENT '是否虚拟奖品',
  `config_template` json NULL COMMENT '奖品配置模板',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '奖品描述',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prize
-- ----------------------------
INSERT INTO `prize` VALUES (1, 'iPhone15', 0, NULL, '水果手机', 0, '2025-05-17 11:33:21', '2025-05-18 11:15:09');
INSERT INTO `prize` VALUES (2, 'vivo x200Pro', 0, NULL, '新一代摄影旗舰机', 1, '2025-05-17 11:34:15', '2025-05-18 11:17:46');
INSERT INTO `prize` VALUES (3, '听泉猫', 0, NULL, '价值2000元', 1, '2025-05-17 11:33:46', '2025-05-17 11:34:48');
INSERT INTO `prize` VALUES (4, '水果手机', 0, NULL, '水果手机，手机中的战斗机', 1, '2025-05-18 10:58:36', '2025-05-18 10:58:36');
INSERT INTO `prize` VALUES (5, '水果手机2', 0, NULL, '水果手机2，手机中的战斗机', 1, '2025-05-18 10:59:19', '2025-05-18 10:59:19');
INSERT INTO `prize` VALUES (6, '水果手机3', 0, NULL, '水果手机3，手机中的战斗机', 1, '2025-05-18 10:59:24', '2025-05-18 10:59:24');
INSERT INTO `prize` VALUES (7, '水果手机4', 0, NULL, '水果手机4，手机中的战斗机', 1, '2025-05-18 10:59:28', '2025-05-18 10:59:28');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'root', '系统管理员');
INSERT INTO `role` VALUES (2, 'user', '用户');
INSERT INTO `role` VALUES (3, 'ccc', '测试');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1, 1);
INSERT INTO `role_menu` VALUES (2, 1, 2);
INSERT INTO `role_menu` VALUES (3, 1, 3);
INSERT INTO `role_menu` VALUES (7, 2, 4);

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '原始文件名',
  `storage_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '存储文件名',
  `file_ext` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '文件扩展名',
  `file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '存储路径',
  `file_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '文件类型',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `upload_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '上传用户名',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, '4.jpg', 'a6d1c4a9-8d8a-40e3-af0a-1af002c239c5.jpg', '.jpg', 'D:\\Project\\Java2025\\uploads\\a6d1c4a9-8d8a-40e3-af0a-1af002c239c5.jpg', 'image/jpeg', 28903, 'cxk', '2025-05-16 08:39:47');
INSERT INTO `sys_file` VALUES (2, '63831429467536.png', '17c68bab-67df-4689-8923-d5f45aefe8fd.png', '.png', 'D:\\Project\\Java2025\\uploads\\17c68bab-67df-4689-8923-d5f45aefe8fd.png', 'image/png', 3380934, 'cxk', '2025-05-22 18:31:20');
INSERT INTO `sys_file` VALUES (3, '63837148923266.png', 'e9101df6-e013-49fb-9dfc-133d4a3b318d.png', '.png', 'D:\\Project\\Java2025\\uploads\\e9101df6-e013-49fb-9dfc-133d4a3b318d.png', 'image/png', 2339247, 'cxk', '2025-05-22 18:31:33');
INSERT INTO `sys_file` VALUES (4, '63837148923266.png', 'daf803e2-662d-424b-9365-46430bcb6927.png', '.png', 'D:\\Project\\Java2025\\uploads\\daf803e2-662d-424b-9365-46430bcb6927.png', 'image/png', 2339247, 'cxk', '2025-05-22 18:31:38');
INSERT INTO `sys_file` VALUES (5, '63842383845398.png', '7a9003dd-8cae-4085-8699-1792a4fe8ac3.png', '.png', 'D:\\Project\\Java2025\\uploads\\7a9003dd-8cae-4085-8699-1792a4fe8ac3.png', 'image/png', 1995938, 'cxk', '2025-05-22 18:31:49');
INSERT INTO `sys_file` VALUES (6, '63848433870832.png', '0e21540d-4bb6-4039-94c9-bd1e7553a434.png', '.png', 'D:\\Project\\Java2025\\uploads\\0e21540d-4bb6-4039-94c9-bd1e7553a434.png', 'image/png', 2007190, 'cxk', '2025-05-22 18:32:02');
INSERT INTO `sys_file` VALUES (7, '63846012671191.png', '19d39ba2-bf9d-4e78-95fb-fda6377fddeb.png', '.png', 'D:\\Project\\Java2025\\uploads\\19d39ba2-bf9d-4e78-95fb-fda6377fddeb.png', 'image/png', 1812379, 'cxk', '2025-05-22 18:34:14');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `accountNonExpired` tinyint(1) NULL DEFAULT NULL,
  `accountNonLocked` tinyint(1) NULL DEFAULT NULL,
  `credentialsNonExpired` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'cxk', '$2a$10$zjG3YU/FPpjOompYYERFu.hgkc6KuKkNojraR3hEEdj8MKX0MNKLi', '1115060927@qq.com', 1, 1, 1, 1);
INSERT INTO `user` VALUES (3, 'ayue', '$2a$10$60FpAgdMi83TsTFZUj3Vz..qTCJtBtE.puvvEu3eba6Z5lbeLQYSq', '3540506054@qq.com', 1, 1, 1, 1);

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`  (
  `user_id` int NOT NULL COMMENT '关联user表的id',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别:0-女,1-男,2-其他',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `bio` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '个人简介',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `fk_user_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户个人信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_profile
-- ----------------------------
INSERT INTO `user_profile` VALUES (2, '姬霓太美', 1, '2025-05-26', '18946175193', '光明路街道十里泉景苑', '/uploads/19d39ba2-bf9d-4e78-95fb-fda6377fddeb.png', '蔡徐坤姬霓太美', '2025-05-16 08:36:25', '2025-05-22 18:34:14');
INSERT INTO `user_profile` VALUES (3, NULL, 1, NULL, NULL, NULL, NULL, NULL, '2025-05-19 14:35:08', '2025-05-19 14:35:08');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 1, 2);
INSERT INTO `user_role` VALUES (3, 2, 2);
INSERT INTO `user_role` VALUES (4, 2, 1);
INSERT INTO `user_role` VALUES (5, 2, 2);
INSERT INTO `user_role` VALUES (6, 2, 3);
INSERT INTO `user_role` VALUES (7, 3, 2);

-- ----------------------------
-- Table structure for winning_record
-- ----------------------------
DROP TABLE IF EXISTS `winning_record`;
CREATE TABLE `winning_record`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` bigint UNSIGNED NOT NULL,
  `activity_id` int UNSIGNED NOT NULL,
  `prize_id` int UNSIGNED NOT NULL,
  `win_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '1待发放 2已发放 3发放失败',
  `mq_msg_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MQ消息ID',
  `is_valid` tinyint NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NULL DEFAULT NULL,
  `participation_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_activity`(`activity_id` ASC) USING BTREE,
  INDEX `idx_valid`(`is_valid` ASC) USING BTREE,
  INDEX `prize_id`(`prize_id` ASC) USING BTREE,
  CONSTRAINT `winning_record_ibfk_1` FOREIGN KEY (`prize_id`) REFERENCES `prize` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of winning_record
-- ----------------------------
INSERT INTO `winning_record` VALUES ('75a06e24ef2b6c2c537bd92d0552391e', 2, 1, 2, '2025-05-22 22:21:17', 1, NULL, 1, '2025-05-22 22:21:17', '2025-05-22 22:21:17', 'dac2eed1-eb6b-4fc6-8e81-858c8959f33c');

SET FOREIGN_KEY_CHECKS = 1;
