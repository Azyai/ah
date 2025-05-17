package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("activity_prize")
public class ActivityPrize extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @NotNull
    // 活动ID
    private Integer activityId;
    
    @NotNull
    // 奖品ID
    private Integer prizeId;
    
    @NotNull
    // 奖品数量
    private Integer totalStock;
    
    @Builder.Default
    // 已使用奖品数量
    private Integer usedStock = 0;
    
    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
    // 奖品概率
    private BigDecimal probability;

    // 排序
    private Integer showOrder;
    
    @TableField(exist = false)
    // 奖品详情
    private Prize prizeDetail; // 关联的奖品详情
}