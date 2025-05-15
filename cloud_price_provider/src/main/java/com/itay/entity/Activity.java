package com.itay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data // getter setter
@Builder // 构建者模式, 可以通过builder来创建对象设置属性
@NoArgsConstructor  // 无参构造函数
@AllArgsConstructor // 有参构造函数
@TableName("activity")
public class Activity extends BaseEntity{

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private Integer type; // 1大转盘 2福袋 3立即开奖

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Builder.Default
    private Integer status = 1; // 1未开始 2进行中 3已结束

    @TableField(typeHandler = FastjsonTypeHandler.class) // 使用自定义的FastjsonTypeHandler，用于处理JSON字段
    private RuleConfig ruleConfig;

    private Integer maxParticipants; // 新增：最大参与人数限制

    @Builder.Default
    private Integer currentParticipants = 0; // 新增：当前参与人数

    @Builder.Default
    private Boolean autoClose = false; // 新增：是否自动关闭


    // 规则配置嵌套类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleConfig {
        private Map<String, Object> drawTypeConfig; // 抽奖类型特有配置
        private LocalDateTime lotteryTime; // 福袋开奖时间
        // 其他自定义规则字段...
    }
}