package com.itay.entity.resp;

import com.itay.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRestrictionResp extends BaseEntity {
    private Integer activityId;
    // 1总次数 2每日次数 3频率限制
    private Integer limitType = 1;
    // 限制的次数
    private Integer limitValue = 1;
    // 间隔秒数
    private Integer intervalSeconds;
}