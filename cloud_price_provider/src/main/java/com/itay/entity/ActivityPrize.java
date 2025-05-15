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
    private Integer activityId;
    
    @NotNull
    private Integer prizeId;
    
    @NotNull
    private Integer totalStock;
    
    @Builder.Default
    private Integer usedStock = 0;
    
    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
    private BigDecimal probability;
    
    private Integer showOrder;
    
    @TableField(exist = false)
    private Prize prizeDetail; // 关联的奖品详情
}