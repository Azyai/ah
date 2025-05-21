package com.itay.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itay.entity.Activity;
import com.itay.entity.Participation;
import com.itay.entity.Prize;
import com.itay.entity.WinningRecord;
import com.itay.mapper.WinningRecordMapper;
import com.itay.service.ActivityService;
import com.itay.service.ParticipationService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RocketMQMessageListener(topic = "fudai-draw-topic", consumerGroup = "fudai-draw-consumer-group")
public class FudaiDrawConsumer implements RocketMQListener<Integer> {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private ActivityService activityService;



    @Autowired
    private WinningRecordMapper winningRecordMapper;


    @Override
    public void onMessage(Integer activityId) {
        // 1. 获取活动信息
        Activity activity = activityService.getById(activityId);
        if (activity == null || !activity.getValid()) {
            throw new RuntimeException("活动不存在");
        }

        // 2. 获取所有参与记录
        LambdaQueryWrapper<Participation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Participation::getActivityId, activityId)
                .eq(Participation::getValid, true);
        List<Participation> participations = participationService.list(queryWrapper);

        // 3. 执行开奖逻辑
        for (Participation participation : participations) {
            Prize prize = participationService.drawPrize(activity);
            if (prize != null) {
                WinningRecord wr = WinningRecord.builder()
                        .userId(participation.getUserId())
                        .activityId(activityId)
                        .prizeId(prize.getId())
                        .status(1)
                        .build();

                int insert = winningRecordMapper.insert(wr);
                if (insert > 0) {
                    participation.setIsWinning(true);
                    participationService.updateById(participation);
                }
            }
        }
    }
}
