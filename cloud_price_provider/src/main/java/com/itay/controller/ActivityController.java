package com.itay.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itay.entity.Activity;
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
@RequestMapping("/prize/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("/selectActivityById")
    public ResultData<Object> selectActivityById(@RequestParam("id") Integer id) {
        if(id == null){
            return ResultData.fail("参数错误");
        }

        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return ResultData.fail("活动不存在");
        }
        return ResultData.success(activity);
    }

    // 按照名称模糊查询活动信息列表
    @GetMapping("/selectActivityByName")
    public ResultData<Object> selectActivityByName(@RequestParam("name") String name) {
        List<Activity> activityByNameList = activityService.getActivityByNameList(name);
        if (activityByNameList.isEmpty()) {
            return ResultData.fail("活动不存在");
        }
        return ResultData.success(activityByNameList);
    }

    // 按照ID修改活动信息
    @PostMapping("/updateActivity")
    // 带上@RequestBody，要发送json格式数据，如果不加就是x-www-form-urlencoded格式数据
    public ResultData<String> updateActivityById(Activity activity) {
        boolean update = activityService.updateById(activity);
        if (update) {
            return ResultData.success("修改成功");
        }
        return ResultData.fail("修改失败");
    }

    @PostMapping("/deleteActivity")
    public ResultData<String> deleteActivityById(@RequestParam("id") Integer id) {
        boolean delete = activityService.removeActivityById(id);
        if (delete) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail("删除失败");
    }


    @PostMapping("deleteActivityByIds")
    // 传入的数据格式为 Content-Type: application/json [1,2,3]
    public ResultData<String> deleteActivityByIds(@RequestBody List<Integer> ids) {
        // 自定义方法进行逻辑删除
        boolean delete = activityService.removeActivityByIds(ids);
        if (delete) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail("删除失败");
    }


    @PostMapping("deleteActivityByIds2")
    // 传入的数据格式为 Content-Type: application/x-www-form-urlencoded ids=1,2,3
    public ResultData<String> deleteActivityByIds2(@RequestParam("ids") List<Integer> ids) {
        // 自定义方法进行逻辑删除
        boolean delete = activityService.removeActivityByIds(ids);
        if (delete) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail("删除失败");
    }

}
