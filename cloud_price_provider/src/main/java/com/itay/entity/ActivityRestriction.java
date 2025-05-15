package com.itay.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("activity_restriction")
public class ActivityRestriction extends BaseEntity{
    @TableId
    private Integer activityId;
    
    @NotNull
    private Integer limitType; // 1总次数 2每日次数 3频率限制
    
    @NotNull
    @Min(1)
    private Integer limitValue;
    
    @Min(1)
    private Integer intervalSeconds; // 间隔秒数
}