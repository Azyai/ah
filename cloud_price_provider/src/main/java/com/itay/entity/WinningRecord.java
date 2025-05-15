package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("winning_record")
public class WinningRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Integer activityId;
    
    @NotNull
    private Integer prizeId;
    
    @Builder.Default
    private LocalDateTime winTime = LocalDateTime.now();
    
    @Builder.Default
    private Integer status = 1; // 1待发放 2已发放 3发放失败
    
    private String mqMsgId;
    
    @TableField(exist = false)
    private Prize prizeInfo; // 关联奖品信息
    
    @TableField(exist = false)
    private Activity activityInfo; // 关联活动信息
}