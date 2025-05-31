package com.itay.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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

    // 1总次数 2每日次数 3频率限制
    private Integer limitType;

    @TableField(fill = FieldFill.INSERT)
    // 限制的次数
    private Integer limitValue = 1;

    // 间隔秒数
    private Integer intervalSeconds;
}