package com.itay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.itay.pojo.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data // getter setter
@Builder // 构建者模式, 可以通过builder来创建对象设置属性
@NoArgsConstructor  // 无参构造函数
@AllArgsConstructor // 有参构造函数
@TableName(value = "activity", autoResultMap = true)
public class Activity extends BaseEntity {

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
    private Map<String,Object> ruleConfig;

    private Integer maxParticipants; // 新增：最大参与人数限制

    @Builder.Default
    private Integer currentParticipants = 0; // 新增：当前参与人数

    @Builder.Default
    private Boolean autoClose = false; // 新增：是否自动关闭


    // 幸运大转盘活动配置
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LuckyWheelConfig {
        private String backgroundImg; // 背景图链接
        private Integer animationSpeed; // 动画速度等级
        private List<Integer> prizeList; // 奖品ID列表
        private Boolean allowShare; // 是否允许分享增加机会
        private Integer shareReward; // 分享奖励次数
    }

    // 福袋活动配置
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FudaiConfig {
        private LocalDateTime lotteryTime; // 开奖时间
        private Boolean showCountdown; // 是否显示倒计时
        private String rules; // 参与规则说明
        private String bannerImg; // 活动横幅图片
    }


    // 立即开奖活动配置
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstantPrizeConfig {
        private Boolean prizeGuarantee; // 是否保底中奖
        private Integer minPrizeId; // 最低奖品ID
        private UserFilter userFilter; // 用户筛选条件
    }

    // 嵌套类：用户筛选条件
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFilter {
        private Integer registerDays; // 注册天数限制
        private Boolean firstLogin; // 是否首次登录
    }


}