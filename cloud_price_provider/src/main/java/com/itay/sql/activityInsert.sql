-- 限时48小时的大转盘活动
-- 允许分享增加抽奖机会
-- 设置5万人参与上限并自动关闭
INSERT INTO `activity` (
    `name`, `type`, `start_time`, `end_time`, `status`,
    `rule_config`, `max_participants`, `auto_close`, `is_valid`
) VALUES (
             '2023双十一幸运大转盘',
             1,
             '2025-11-11 00:00:00',
             '2025-11-12 23:59:59',
             2,
             '{
               "draw_type_config": {
                 "background_img": "https://example.com/lucky-wheel.jpg",
                 "animation_speed": 3,
                 "prize_list": [1, 2, 3, 4, 5]
               },
               "allow_share": true,
               "share_reward": 1
             }',
             50000,
             1,
             1
         );

-- 春节主题的定时开奖福袋
-- 活动期间可参与，元宵节统一开奖
-- 无人数限制（max_participants为NULL）
INSERT INTO `activity` (
    `name`, `type`, `start_time`, `end_time`, `status`,
    `rule_config`, `current_participants`, `is_valid`
) VALUES (
             '2024春节神秘福袋',
             2,
             '2025-01-20 00:00:00',
             '2025-02-05 23:59:59',
             1,
             '{
               "lottery_time": "2025-02-10 20:00:00",
               "show_countdown": true,
               "rules": "每人限参与1次",
               "banner_img": "https://example.com/spring-fudai.jpg"
             }',
             0,
             1
         );


-- 新用户注册后立即开奖
-- 10万人参与上限但不自动关闭
-- 包含奖品保底机制
INSERT INTO `activity` (
    `name`, `type`, `start_time`, `end_time`, `status`,
    `rule_config`, `max_participants`, `auto_close`, `is_valid`
) VALUES (
             '新用户注册礼包',
             3,
             '2025-09-01 00:00:00',
             '2025-12-31 23:59:59',
             2,
             '{
               "prize_guarantee": true,
               "min_prize_id": 6,
               "user_filter": {
                 "register_days": 1,
                 "first_login": true
               }
             }',
             100000,
             0,
             1
         );

-- 查看JSON字段的特定值，特殊字段处理
SELECT
    name,
    rule_config->>"$.draw_type_config.background_img" AS wheel_bg,
    rule_config->>"$.lottery_time" AS fudai_time
FROM activity;


SELECT id, rule_config FROM activity WHERE id = 1;


INSERT INTO `activity` (
    `name`,
    `type`,
    `start_time`,
    `end_time`,
    `status`,
    `rule_config`,
    `max_participants`,
    `auto_close`,
    `is_valid`
) VALUES (
             '2025夏季幸运大转盘',
             1,
             '2025-07-01 00:00:00',
             '2025-07-03 23:59:59',
             2,
             '{
               "background_img": "https://example.com/summer-wheel.jpg",
               "animation_speed": 2,
               "prize_list": [6, 7, 8, 9, 10],
               "allow_share": false,
               "share_reward": 0
             }',
             100000,
             1,
             1
         );



INSERT INTO `activity` (
    `name`,
    `type`,
    `start_time`,
    `end_time`,
    `status`,
    `rule_config`,
    `current_participants`,
    `is_valid`
) VALUES (
             '2025元宵节福袋惊喜',
             2,
             '2025-02-10 00:00:00',
             '2025-02-15 23:59:59',
             1,
             '{
               "lottery_time": "2025-02-16 20:00:00",
               "show_countdown": true,
               "rules": "每人限参与2次",
               "banner_img": "https://example.com/lunar-blessing.jpg"
             }',
             0,
             1
         );


INSERT INTO `activity` (
    `name`,
    `type`,
    `start_time`,
    `end_time`,
    `status`,
    `rule_config`,
    `max_participants`,
    `auto_close`,
    `is_valid`
) VALUES (
             '老用户回归专属礼包',
             3,
             '2025-10-01 00:00:00',
             '2025-10-31 23:59:59',
             2,
             '{
               "prize_guarantee": true,
               "min_prize_id": 5,
               "user_filter": {
                 "register_days": 30,
                 "first_login": false
               }
             }',
             50000,
             0,
             1
         );
