package com.itay.dto.request;

import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import lombok.Data;

import java.util.List;

/**
 * @author ly111
 * 创建活动请求封装
 */
@Data
public class CreateActivityRequest {
    private Activity activity;
    private List<ActivityPrize> prizes;
    private ActivityRestriction activityRestriction;
}