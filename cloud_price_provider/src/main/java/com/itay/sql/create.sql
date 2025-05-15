-- 1. 活动表（抽奖活动核心配置）
CREATE TABLE `activity` (
                            `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(100) NOT NULL COMMENT '活动名称',
                            `type` TINYINT NOT NULL COMMENT '1大转盘 2福袋 3立即开奖',
                            `start_time` DATETIME NOT NULL COMMENT '开始时间',
                            `end_time` DATETIME NOT NULL COMMENT '结束时间',
                            `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1未开始 2进行中 3已结束',
                            `rule_config` JSON NOT NULL COMMENT '{
    "draw_type_config":{},      -- 抽奖类型特有配置
    "lottery_time": "2023-01-01 12:00:00" -- 福袋开奖时间
  }',
                            `max_participants` INT UNSIGNED DEFAULT NULL COMMENT '最大参与人数限制（NULL表示无限制）',
                            `current_participants` INT UNSIGNED DEFAULT 0 COMMENT '当前参与人数',
                            `auto_close` TINYINT DEFAULT 0 COMMENT '是否达到人数自动关闭',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            INDEX `idx_time_status` (`start_time`, `end_time`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 奖品库表（全平台奖品库）
CREATE TABLE `prize` (
                         `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(100) NOT NULL COMMENT '奖品名称',
                         `is_virtual` TINYINT NOT NULL DEFAULT 0 COMMENT '是否虚拟奖品',
                         `config_template` JSON COMMENT '奖品配置模板',
                         `description` TEXT COMMENT '奖品描述',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 活动奖品关联表
CREATE TABLE `activity_prize` (
                                  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `activity_id` INT UNSIGNED NOT NULL,
                                  `prize_id` INT UNSIGNED NOT NULL,
                                  `total_stock` INT NOT NULL COMMENT '总库存',
                                  `used_stock` INT NOT NULL DEFAULT 0 COMMENT '已用库存',
                                  `probability` DECIMAL(5,4) NOT NULL COMMENT '中奖概率',
                                  `show_order` INT COMMENT '展示顺序',
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_activity` (`activity_id`),
                                  FOREIGN KEY (`prize_id`) REFERENCES `prize`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 活动限制规则表
CREATE TABLE `activity_restriction` (
                                        `activity_id` INT UNSIGNED NOT NULL PRIMARY KEY,
                                        `limit_type` TINYINT NOT NULL COMMENT '1总次数 2每日次数 3频率限制',
                                        `limit_value` INT NOT NULL COMMENT '限制值',
                                        `interval_seconds` INT COMMENT '间隔秒数（用于频率限制）',
                                        FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 用户参与记录表（分表建议）
CREATE TABLE `participation` (
                                 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `user_id` BIGINT UNSIGNED NOT NULL,
                                 `activity_id` INT UNSIGNED NOT NULL,
                                 `participate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `ip` VARCHAR(45) NOT NULL,
                                 `device_fingerprint` VARCHAR(100) COMMENT '设备指纹',
                                 `is_winning` TINYINT DEFAULT 0 COMMENT '是否中奖',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_user_activity` (`user_id`, `activity_id`),
                                 INDEX `idx_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 中奖记录表
CREATE TABLE `winning_record` (
                                  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `user_id` BIGINT UNSIGNED NOT NULL,
                                  `activity_id` INT UNSIGNED NOT NULL,
                                  `prize_id` INT UNSIGNED NOT NULL,
                                  `win_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1待发放 2已发放 3发放失败',
                                  `mq_msg_id` VARCHAR(50) COMMENT 'MQ消息ID',
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_status` (`status`),
                                  INDEX `idx_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;