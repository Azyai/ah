package com.itay.controller;

import com.itay.entity.Activity;
import com.itay.entity.Prize;
import com.itay.request.ParticipationRequest;
import com.itay.resp.ResultData;
import com.itay.service.ActivityService;
import com.itay.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draw/participation")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private ActivityService activityService;

    @PostMapping("/participate")
    @Transactional
    public ResultData<String> participate(@RequestBody ParticipationRequest participationRequest) {
        Long userId = participationRequest.getUserId();
        Integer activityId = participationRequest.getActivityId();
        String ip = participationRequest.getIp();
        String deviceFingerprint = participationRequest.getDeviceFingerprint();

        System.out.println("activityId: " + activityId);

        // 存储参与记录
        boolean success = participationService.addParticipate(userId, activityId, ip, deviceFingerprint);
        if (!success) {
            return ResultData.fail("参与失败");
        }

        // 2.获取活动信息
        Activity activity = activityService.getById(activityId);
        if (activity == null || !activity.getValid()) {
            return ResultData.fail("活动不存在");
        }

        // 3. 如果是立即开奖型，直接开奖
        if(activity.getType() == 1 ||activity.getType() == 3){
            Prize prize = participationService.drawPrize(activity);
            if(prize != null){
                return ResultData.success("恭喜您中奖了,奖品是："+ prize.getName());
            }else {
                return ResultData.success("参与成功，但是未中奖!");
            }
        }

        // 4.如果是福袋型，返回成功参与的信息，等待rocketMQ延时消息触发开奖
        return ResultData.success("参与成功，请等待开奖结果！");

    }

    @GetMapping("/getActivityById")
    ResultData<Activity> getActivityById(@RequestParam("activityId") Integer activityId){
        return ResultData.success(activityService.getById(activityId));
    }


}
