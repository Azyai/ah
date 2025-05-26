package com.itay.dto.request;

import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ly111
 * 创建活动请求封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequest {
    private Activity activity;
    private List<ActivityPrize> prizes;
    private ActivityRestriction activityRestriction;
}