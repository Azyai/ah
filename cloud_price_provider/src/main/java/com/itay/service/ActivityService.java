package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.dto.request.CreateActivityRequest;
import com.itay.dto.response.ActivityResp;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import com.itay.entity.resp.ActivityInfoResp;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;

import java.util.List;

/**
 * @author ly111
 */
public interface ActivityService extends IService<Activity> {


    Boolean addActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction);

    boolean deleteBatchActivity(List<Integer> ids);

    boolean updateActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction);

    CommonResponse<ActivityResp> selectActivity(String name, Integer page, Integer limit);

    CommonResponse<ActivityInfoResp> selectActivityInfo(String name, Integer page, Integer limit);
}
