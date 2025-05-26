package com.itay.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itay.dto.request.CreateActivityRequest;
import com.itay.dto.response.ActivityResp;
import com.itay.entity.Activity;
import com.itay.entity.resp.ActivityInfoResp;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import com.itay.service.ActivityService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author ly111
 * date  2025/05/15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@RestController
@RequestMapping("/draw/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    /**
     * 最终版本的增删改查，之前的增删改查只是基于单表的
     * 现在需要考虑多表关联，即创建活动信息时，也要创建与之关联的活动奖品、活动限制等
     */

    // 添加活动信息
    @PostMapping("/addActivity")
    public ResultData<String> addActivity(@RequestBody CreateActivityRequest request) {
        Boolean b = activityService.addActivity(request.getActivity(), request.getPrizes(), request.getActivityRestriction());
        if (b) {
            return ResultData.success("添加成功");
        }
        return ResultData.fail("添加失败");
    }

    // 批量删除活动信息:即可单个删除，也可以批量删除
    @PostMapping("/deleteBatchActivity")
    public ResultData<String> deleteBatchActivity(@RequestParam("ids") List<Integer> ids) {
        boolean delete = activityService.deleteBatchActivity(ids);
        if (delete) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail("删除失败");
    }

    // 修改活动信息
    @PostMapping("/updateActivity")
    public ResultData<String> updateActivity(@RequestBody CreateActivityRequest request) {
        boolean update = activityService.updateActivity(request.getActivity(), request.getPrizes(), request.getActivityRestriction());
        if (update) {
            return ResultData.success("修改成功");
        }
        return ResultData.fail("修改失败");
    }

    @GetMapping("/selectActivity")
    public ResultData<CommonResponse<ActivityResp>> selectActivity(NameRequest nameRequest) {
        CommonResponse<ActivityResp> activityRespCommonResponse = activityService.selectActivity(nameRequest.getName(), nameRequest.getPage(), nameRequest.getLimit());
        return ResultData.success(activityRespCommonResponse);
    }

    @GetMapping("/selectActivityInfo")
    public ResultData<CommonResponse<ActivityInfoResp>> selectActivityInfo(NameRequest nameRequest) {
        CommonResponse<ActivityInfoResp> activityInfoRespCommonResponse = activityService.selectActivityInfo(nameRequest.getName(), nameRequest.getPage(), nameRequest.getLimit());
        return ResultData.success(activityInfoRespCommonResponse);
    }


}
