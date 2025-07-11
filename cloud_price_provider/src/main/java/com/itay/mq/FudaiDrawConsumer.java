package com.itay.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itay.entity.*;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.mapper.WinningRecordMapper;
import com.itay.service.ActivityService;
import com.itay.service.ParticipationService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RocketMQMessageListener(topic = "fd-draw-topic", consumerGroup = "fd-draw-consumer-group")
public class FudaiDrawConsumer implements RocketMQListener<Integer> {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WinningRecordMapper winningRecordMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;


    @Override
    @Transactional
    public void onMessage(Integer activityId) {

        System.out.println("执行福袋开奖：" + activityId);

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

                    System.out.println("奖品id：" + wr.getPrizeId());

                    // 更改activityPrize中的奖品数量
                    ActivityPrize actPrize = activityPrizeMapper.selectById(wr.getActivityId());

                    System.out.println("actPrize: " + actPrize);
                    System.out.println("奖品数量总数：" + actPrize.getTotalStock());
                    System.out.println("奖品数量使用更新前：" + actPrize.getUsedStock());

                    if(actPrize.getUsedStock() + 1 <= actPrize.getTotalStock()){
                        System.out.println("奖品数量使用更新成功");
                        actPrize.setUsedStock(actPrize.getUsedStock() + 1);
                        int update = activityPrizeMapper.updateById(actPrize);
                        if(update < 0){
                            throw new RuntimeException("奖品数量更新失败");
                        }
                    }

                }
            }
        }
    }
}
