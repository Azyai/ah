package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.entity.Participation;
import com.itay.mapper.ActivityMapper;
import com.itay.mapper.ParticipationMapper;
import com.itay.service.ParticipationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParticipationServiceImpl extends ServiceImpl<ParticipationMapper, Participation> implements ParticipationService {

    @Resource
    ActivityMapper activityMapper;

    @Resource
    ParticipationMapper participationMapper;

    @Override
    public boolean addParticipate(Long userId, Integer activityId, String ip,String deviceFingerprint) {
        // 1.验证活动有效性
        Activity activity = activityMapper.selectById(activityId);
        if(activity == null || !activity.getValid()){
            throw new RuntimeException("活动不存在或者已经删除");
        }

        if(LocalDateTime.now().isBefore(activity.getStartTime())){
            throw new RuntimeException("活动尚未开始");
        }

        if(LocalDateTime.now().isAfter(activity.getEndTime())){
            throw new RuntimeException("活动已经结束");
        }

        // 2. 使用ActivityCounter进行计数控制



        return true;
    }
}