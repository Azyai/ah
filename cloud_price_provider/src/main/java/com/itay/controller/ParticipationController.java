package com.itay.controller;

import com.itay.entity.Activity;
import com.itay.entity.resp.ParticipationResp;
import com.itay.entity.Prize;
import com.itay.entity.WinningRecord;
import com.itay.request.IdRequest;
import com.itay.request.ParticipationRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import com.itay.service.ActivityService;
import com.itay.service.ParticipationService;
import com.itay.service.WinningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/draw/participation")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WinningRecordService winningRecordService;

    @PostMapping("/participate")
    @Transactional
    public ResultData<String> participate(@RequestBody ParticipationRequest participationRequest) {
        Long userId = participationRequest.getUserId();
        Integer activityId = participationRequest.getActivityId();
        String ip = participationRequest.getIp();
        String deviceFingerprint = participationRequest.getDeviceFingerprint();

        // 生成 UUID 作为参与记录的 ID
        String participationId = UUID.randomUUID().toString();

        // 获取活动信息
        Activity activity = activityService.getById(activityId);
        if (activity == null || !activity.getValid()) {
            return ResultData.fail("活动不存在");
        }

        // 判断活动是否为福袋型，如果是福袋型只能参与一次
        if (activity.getType() == 2) {
             if (participationService.hasParticipated(userId, activityId)) {
                 return ResultData.fail("401","您已经参与过该活动，请勿重复参与");
             }
        }

        // 存储参与记录
        boolean success = participationService.addParticipate(participationId, userId, activityId, ip, deviceFingerprint);
        if (!success) {
            return ResultData.fail("401","参与失败");
        }

        // 如果是立即开奖型，直接开奖
        if (activity.getType() == 1 || activity.getType() == 3) {
            Prize prize = participationService.drawPrize(activity);
            if (prize != null) {
                // 更新参与记录的中奖状态
                participationService.updateWinningStatus(participationId, true);

                WinningRecord winningRecord = WinningRecord.builder()
                        .userId(userId)
                        .activityId(activityId)
                        .prizeId(prize.getId())
                        .status(1)
                        .participationId(participationId)  // 关联中奖的参与记录
                        .build();

                boolean b = winningRecordService.addAndUpdateActivity(winningRecord);
                if (!b){
                    throw new RuntimeException("添加中奖记录失败,更新记录失败！");
                }

                return ResultData.success("恭喜您中奖了,奖品是：" + prize.getName());
            } else {
                return ResultData.success("参与成功，但是未中奖!");
            }
        }

        // 如果是福袋型，返回成功参与的信息，等待rocketMQ延时消息触发开奖
        return ResultData.success("参与成功，请等待开奖结果！");
    }


    @GetMapping("/getActivityById")
    ResultData<Activity> getActivityById(@RequestParam("activityId") Integer activityId) {
        return ResultData.success(activityService.getById(activityId));
    }

    @PostMapping("/getParticipation")
    // 查询肯定是分页查询
    public ResultData<CommonResponse<ParticipationResp>> getParticipation(@RequestBody IdRequest idRequest){
        System.out.println("idRequest: " + idRequest);
        CommonResponse<ParticipationResp> participationResps = participationService.selectParticipationResp(idRequest);
        if (participationResps == null){
            throw new RuntimeException("您当前还没有参与任何活动");
        }
        return ResultData.success(participationResps);
    }


}
