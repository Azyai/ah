package com.itay.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCounter extends BaseEntity {
    private Integer activityId; // 活动ID
    private String redisKey; // activity:participants:count:{activityId}
    private Integer currentCount; // 当前参与人数
    private Integer maxLimit; // 最大参与人数
    private Boolean isReached; // 是否已达上限
}