package com.itay.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCounter {
    private Integer activityId;
    private String redisKey; // activity:participants:count:{activityId}
    private Integer currentCount;
    private Integer maxLimit;
    private Boolean isReached; // 是否已达上限
}