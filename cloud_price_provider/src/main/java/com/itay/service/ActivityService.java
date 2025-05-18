package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.dto.request.PrizeRequest;
import com.itay.entity.Activity;

import java.util.List;

/**
 * @author ly111
 */
public interface ActivityService extends IService<Activity> {

    List<Activity> getActivityByNameList(String name);

    Activity getActivityById(Integer id);

    boolean removeActivityById(Integer id);

    boolean removeActivityByIds(List<Integer> ids);

    List<Activity> selectActivityPageByName(PrizeRequest prizeRequest);
}
