CREATE TABLE `activity` (
                            `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(100) NOT NULL COMMENT '活动名称',
                            `type` TINYINT NOT NULL COMMENT '1大转盘 2福袋 3立即开奖',
                            `start_time` DATETIME NOT NULL COMMENT '开始时间',
                            `end_time` DATETIME NOT NULL COMMENT '结束时间',
                            `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1未开始 2进行中 3已结束',
                            `rule_config` JSON NOT NULL COMMENT '抽奖规则配置',
                            `max_participants` INT UNSIGNED DEFAULT NULL COMMENT '最大参与人数限制',
                            `current_participants` INT UNSIGNED DEFAULT 0 COMMENT '当前参与人数',
                            `auto_close` TINYINT DEFAULT 0 COMMENT '是否自动关闭',
                            `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            INDEX `idx_time_status` (`start_time`, `end_time`, `status`),
                            INDEX `idx_valid` (`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `prize` (
                         `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(100) NOT NULL COMMENT '奖品名称',
                         `is_virtual` TINYINT NOT NULL DEFAULT 0 COMMENT '是否虚拟奖品',
                         `config_template` JSON COMMENT '奖品配置模板',
                         `description` TEXT COMMENT '奖品描述',
                         `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         INDEX `idx_valid` (`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `activity_prize` (
                                  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `activity_id` INT UNSIGNED NOT NULL,
                                  `prize_id` INT UNSIGNED NOT NULL,
                                  `total_stock` INT NOT NULL COMMENT '总库存',
                                  `used_stock` INT NOT NULL DEFAULT 0 COMMENT '已用库存',
                                  `probability` DECIMAL(5,4) NOT NULL COMMENT '中奖概率',
                                  `show_order` INT COMMENT '展示顺序',
                                  `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_activity` (`activity_id`),
                                  INDEX `idx_valid` (`is_valid`),
                                  FOREIGN KEY (`prize_id`) REFERENCES `prize`(`id`),
                                  CONSTRAINT `fk_activity_prize` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `activity_restriction` (
                                        `activity_id` INT UNSIGNED NOT NULL,
                                        `limit_type` TINYINT NOT NULL COMMENT '1总次数 2每日次数 3频率限制',
                                        `limit_value` INT NOT NULL COMMENT '限制值',
                                        `interval_seconds` INT COMMENT '间隔秒数',
                                        `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`activity_id`),
                                        INDEX `idx_valid` (`is_valid`),
                                        FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `participation` (
                                 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `user_id` BIGINT UNSIGNED NOT NULL,
                                 `activity_id` INT UNSIGNED NOT NULL,
                                 `participate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `ip` VARCHAR(45) NOT NULL,
                                 `device_fingerprint` VARCHAR(100) COMMENT '设备指纹',
                                 `is_winning` TINYINT DEFAULT 0 COMMENT '是否中奖',
                                 `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_user_activity` (`user_id`, `activity_id`),
                                 INDEX `idx_activity` (`activity_id`),
                                 INDEX `idx_valid` (`is_valid`),
                                 FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `winning_record` (
                                  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `user_id` BIGINT UNSIGNED NOT NULL,
                                  `activity_id` INT UNSIGNED NOT NULL,
                                  `prize_id` INT UNSIGNED NOT NULL,
                                  `win_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1待发放 2已发放 3发放失败',
                                  `mq_msg_id` VARCHAR(50) COMMENT 'MQ消息ID',
                                  `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '1有效 0删除',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_status` (`status`),
                                  INDEX `idx_activity` (`activity_id`),
                                  INDEX `idx_valid` (`is_valid`),
                                  FOREIGN KEY (`prize_id`) REFERENCES `prize`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;