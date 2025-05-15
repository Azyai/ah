package com.itay.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itay.entity.Activity;
import com.itay.resp.ResultData;
import com.itay.service.ActivityService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@RestController
@RequestMapping("/prize/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("/selectActivityById")
    public ResultData<Activity> selectActivityById(@RequestParam("id") Integer id) {
        Activity activity = activityService.getById(id);
        return ResultData.success(activity);
    }

}
