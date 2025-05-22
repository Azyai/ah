package com.itay.controller;


import com.itay.apis.PriceApi;
import com.itay.apis.UserApi;
import com.itay.entity.Activity;
import com.itay.entity.resp.ParticipationResp;
import com.itay.request.IdRequest;
import com.itay.request.ParticipationRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/draw/participation")
public class ParticipationConsumerController {

    @Autowired
    private PriceApi participateApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping("/participate")
    public ResultData<String> participate(
            ParticipationRequest participationRequest,
            HttpServletRequest httpServletRequest) {

        // 获取用户ID和IP地址
        String username = httpServletRequest.getHeader("X-User");
        Long userId = userApi.getUserId(username);
        participationRequest.setUserId(userId);


        String ip = httpServletRequest.getRemoteAddr();
        System.out.println("userId: "+ userId);
        System.out.println("ip: "+ ip);
        System.out.println("activityId " + participationRequest.getActivityId());
        System.out.println("Finger: " + participationRequest.getDeviceFingerprint());
        participationRequest.setIp(ip);

        // 1. 调用 Feign 接口存储参与信息
        ResultData<String> result = participateApi.participate(participationRequest);
        if (!result.getCode().equals("200")) {
            return result; // 如果存储失败，直接返回错误信息
        }

        // 2. 获取活动信息
        ResultData<Activity> activityResult = participateApi.getActivityById(participationRequest.getActivityId());
        if (!activityResult.getCode().equals("200") || activityResult.getData() == null) {
            return ResultData.fail("活动不存在");
        }
        Activity activity = activityResult.getData();

        // 3. 如果是福袋型，发送 RocketMQ 延时消息
        if (activity.getType() == 2) {
            LocalDateTime endTime = activity.getEndTime();
            long delayTime = Duration.between(LocalDateTime.now(), endTime).toMillis();

            rocketMQTemplate.syncSend(
                    "fudai-draw-topic",
                    MessageBuilder.withPayload(participationRequest.getActivityId()).build(),
                    delayTime,
                    RocketMQDelayLevel.getDelayLevel(delayTime)
            );
        }

        return ResultData.success(result.getData());
    }

    private static class RocketMQDelayLevel {
        private static final int[] DELAY_LEVELS = {
                1, 5, 10, 30, 60, 120, 180, 240, 300, 360, 420, 480
        };

        public static int getDelayLevel(long delayTimeMillis) {
            long delayMinutes = delayTimeMillis / (60 * 1000);
            for (int i = 0; i < DELAY_LEVELS.length; i++) {
                if (delayMinutes <= DELAY_LEVELS[i]) {
                    return i + 1; // RocketMQ 的延迟级别从 1 开始
                }
            }
            return DELAY_LEVELS.length; // 最大延迟级别
        }
    }

    @GetMapping("/consumer/getServerPort")
    public ResultData<String> getServerPort() {
        return participateApi.getServerPort();
    }

    @GetMapping("/getActivityById")
    ResultData<Activity> getActivityById(@RequestParam("activityId") Integer activityId){
        return participateApi.getActivityById(activityId);
    }

    @PostMapping("/getParticipation")
    ResultData<CommonResponse<ParticipationResp>> getParticipation(@RequestBody IdRequest idRequest,HttpServletRequest httpServletRequest){

        String username = httpServletRequest.getHeader("X-User");
        Long userId = userApi.getUserId(username);
        idRequest.setId(userId);

        return participateApi.getParticipation(idRequest);
    }

}
