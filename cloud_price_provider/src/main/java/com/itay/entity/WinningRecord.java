package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itay.pojo.BaseEntity;
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
public class WinningRecord extends BaseEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;  // 改为 UUID 类型

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

    @NotNull
    private String participationId;  // 新增字段，关联中奖的参与记录

    @TableField(exist = false)
    private Prize prizeInfo; // 关联奖品信息

    @TableField(exist = false)
    private Activity activityInfo; // 关联活动信息
}
