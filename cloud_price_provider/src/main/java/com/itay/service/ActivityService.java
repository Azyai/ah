package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.dto.request.CreateActivityRequest;
import com.itay.dto.response.ActivityResp;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;

import java.util.List;

/**
 * @author ly111
 */
public interface ActivityService extends IService<Activity> {

//    List<Activity> getActivityByNameList(String name);
//
//    Activity getActivityById(Integer id);
//
//    boolean removeActivityById(Integer id);
//
//    boolean removeActivityByIds(List<Integer> ids);
//
//    CommonResponse<Activity> selectActivityPageByName(NameRequest prizeRequest);
//
//    boolean saveActivityWithPrizes(Activity activity, List<ActivityPrize> prizes);


    Boolean addActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction);

    boolean deleteBatchActivity(List<Integer> ids);

    boolean updateActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction);

    CommonResponse<ActivityResp> selectActivity(String name, Integer page, Integer limit);
}
