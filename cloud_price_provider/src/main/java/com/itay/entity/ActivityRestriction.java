package com.itay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.itay.pojo.BaseEntity;
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
public class ActivityRestriction extends BaseEntity {
    @TableId
    private Integer activityId;
    
    @NotNull
    @TableField(fill = FieldFill.INSERT)
    // 1总次数 2每日次数 3频率限制
    private Integer limitType = 1;
    
    @NotNull
    @Min(1)
    @TableField(fill = FieldFill.INSERT)
    // 限制的次数
    private Integer limitValue = 1;

    @Min(1)
    // 间隔秒数
    private Integer intervalSeconds;
}