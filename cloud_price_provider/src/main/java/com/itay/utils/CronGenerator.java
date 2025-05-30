package com.itay.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CRON表达式生成工具类
 * 支持从LocalDateTime生成精确到秒的CRON表达式
 */
public class CronGenerator {

    private static final Logger log = LoggerFactory.getLogger(CronGenerator.class);

    /**
     * 根据指定时间生成CRON表达式（精确到秒）
     *
     * @param triggerTime 触发时间点
     * @return CRON表达式字符串
     */
    public static String generateCronExpression(LocalDateTime triggerTime) {
        // 使用标准的 CRON 格式：秒 分 时 日 月 周几 年
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("s m H d M ? yyyy");
        String cron = triggerTime.format(formatter);
        log.info("Generated CRON expression for time {}: {}", triggerTime, cron);
        return cron;
    }

    /**
     * 构建基于活动结束时间的CRON表达式
     *
     * @param activityEndTime 活动结束时间
     * @return CRON表达式字符串
     */
    public static String getCronFromActivityEndTime(LocalDateTime activityEndTime) {
        return generateCronExpression(activityEndTime);
    }
}
