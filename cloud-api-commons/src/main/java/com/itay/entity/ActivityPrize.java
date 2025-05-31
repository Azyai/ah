package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class ActivityPrize extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer activityId;
    private Integer prizeId;
    private Integer totalStock;

    @Builder.Default
    private Integer usedStock = 0;
    private BigDecimal probability;
    
    private Integer showOrder;

}