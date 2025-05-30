package com.itay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.Participation;
import com.itay.entity.Prize;
import com.itay.entity.resp.ParticipationResp;
import com.itay.mapper.ActivityMapper;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.mapper.ParticipationMapper;
import com.itay.mapper.PrizeMapper;
import com.itay.pojo.ActivityCounter;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;
import com.itay.service.ParticipationService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ParticipationServiceImpl extends ServiceImpl<ParticipationMapper, Participation> implements ParticipationService {

    @Resource
    ActivityMapper activityMapper;

    @Resource
    ParticipationMapper participationMapper;

    @Resource
    PrizeMapper prizeMapper;

    @Resource
    RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private TaskScheduler taskScheduler;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;


    @Override
    public boolean addParticipate(String participationId, Long userId, Integer activityId, String ip, String deviceFingerprint) {
        Participation participation = Participation.builder()
                .id(participationId)
                .userId(userId)
                .activityId(activityId)
                .ip(ip)
                .deviceFingerprint(deviceFingerprint)
                .build();

        // 2.更改activity中参与人数，如果活动人数达到上限，则结束活动
        Activity activity = activityMapper.selectById(activityId);
        if (activity.getCurrentParticipants() + 1 <= activity.getMaxParticipants()) {
            activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
            int update = activityMapper.updateById(activity);
            if (update < 0) {
                throw new RuntimeException("活动人数更新失败");
            }
        } else {
            activity.setStatus(2);
            int update = activityMapper.updateById(activity);
            if (update < 0) {
                throw new RuntimeException("活动状态更新失败");
            }
        }

        return participationMapper.insert(participation) > 0;
    }

    @Override
    public void updateWinningStatus(String participationId, boolean isWinning) {
        Participation participation = new Participation();
        participation.setId(participationId);
        participation.setIsWinning(isWinning);
        participationMapper.updateById(participation);
    }


//    @Override
//    public boolean addParticipate(Long userId, Integer activityId, String ip, String deviceFingerprint) {
//        // 1.验证活动有效性
//        Activity activity = activityMapper.selectById(activityId);
//        if (activity == null || !activity.getValid()) {
//            throw new RuntimeException("活动不存在或者已经删除");
//        }
//
//        if (LocalDateTime.now().isBefore(activity.getStartTime())) {
//            throw new RuntimeException("活动尚未开始");
//        }
//
//        if (LocalDateTime.now().isAfter(activity.getEndTime())) {
//            throw new RuntimeException("活动已经结束");
//        }
//
//        // 2. 使用ActivityCounter进行计数控制
//        ActivityCounter counter = new ActivityCounter().builder()
//                .activityId(activityId)
//                .redisKey("activity:participants:count:" + activityId)
//                .currentCount(activity.getCurrentParticipants())
//                .maxLimit(activity.getMaxParticipants())
//                .build();
//
//        if (!updateCounter(counter, activity)) {
//            throw new RuntimeException("活动人数已满");
//        }
//
//        // 3.判断活动参与限制,如果存在一次参与，则无法参与

    /// /        LambdaQueryWrapper<Participation> queryWrapper = new LambdaQueryWrapper<>();
    /// /        queryWrapper.eq(Participation::getUserId,userId)
    /// /                .eq(Participation::getActivityId,activityId);
    /// /        if(participationMapper.selectOne(queryWrapper) != null){
    /// /            throw new RuntimeException("用户已经参与过活动");
    /// /        }`
//
//        // 4.保存参与记录
//        Participation participation = Participation.builder()
//                .userId(userId)
//                .activityId(activityId)
//                .ip(ip)
//                .deviceFingerprint(deviceFingerprint)
//                .build();
//
//        int insert = participationMapper.insert(participation);
//
//        return insert > 0;
//    }
    @Override
    @Transactional
    public boolean updateCounter(ActivityCounter counter, Activity activity) {
        // 先存入MySQL，再延迟双删Redis缓存

        // 1.先存入MySQL中
        activity.setCurrentParticipants(counter.getCurrentCount() + 1);
        if (activityMapper.updateById(activity) <= 0) {
            throw new RuntimeException("活动更新失败，事务将回滚");
        }

        // 2.删除Redis缓存
        redisTemplate.delete(counter.getRedisKey());

        // 3.延迟双删Redis缓存
        taskScheduler.schedule(() -> {
            redisTemplate.delete(counter.getRedisKey());
        }, new Date(System.currentTimeMillis() + 1000)); // 延迟1秒执行

        return true;
    }

    @Override
    public int getParticipateCount(Integer activityId) {
        String redisKey = "activity:participants:count:" + activityId;

        // 1.先查询Redis
        Integer count = redisTemplate.opsForValue().get(redisKey);
        if (count != null) {
            return count;
        }

        // 2.如果Redis中没有数据，则查询MySQL
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }

        // 3.将MySQL中的数据写入Redis中
        redisTemplate.opsForValue().set(redisKey, activity.getCurrentParticipants());
        return activity.getCurrentParticipants();
    }

    @Override
    public Prize drawPrize(Activity activity) {
        // 1. 获取活动关联的奖品列表
        List<ActivityPrize> prizeList = activityPrizeMapper.selectList(
                new LambdaQueryWrapper<ActivityPrize>()
                        .eq(ActivityPrize::getActivityId, activity.getId())
                        .eq(ActivityPrize::getValid, true)
        );

        // 2. 将activityPrize关联的奖品详情填充到ActivityPrize中
        prizeList.forEach(prize -> {
            prize.setPrizeDetail(
                    prizeMapper.selectById(prize.getPrizeId())
            );
        });

        System.out.println("prizeList: " + prizeList);
        if (prizeList.isEmpty()) {
            throw new RuntimeException("活动没有奖品");
        }

        // 2.计算总的概率
        BigDecimal totalProbability = prizeList.stream()
                .map(ActivityPrize::getProbability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("totalProbability: " + totalProbability);

        // 3.生产随机数
        BigDecimal randomValue = BigDecimal.valueOf(Math.random());

        // 4.根据概率选择奖品，cumulativeProbability表示当前奖品的累计概率
        // cumulativeProbability表示当前奖品的累计概率

        if (randomValue.compareTo(totalProbability) < 0) {
            BigDecimal cumulativeProbability = BigDecimal.ZERO;
            for (ActivityPrize prize : prizeList) {

                // 遍历奖品列表，累加每个奖品的概率
                // 当随机值小于当前累计概率时，返回对应的奖品
                // 这种方法确保了每个奖品的中奖概率与其设置的概率值成正比

                cumulativeProbability = cumulativeProbability.add(prize.getProbability());
                System.out.println("cumulativeProbability: " + cumulativeProbability);

                System.out.println("randomValue: " + randomValue);
                System.out.println(randomValue.compareTo(cumulativeProbability) + " ------");
                if (randomValue.compareTo(cumulativeProbability) < 0) {
                    // 奖品被选中，返回中奖奖品
                    return prize.getPrizeDetail();
                }
            }

        }

        return null; // 未中奖
    }

    @Override
    public CommonResponse<ParticipationResp> selectParticipationResp(IdRequest idRequest) {
        IPage<Participation> page = new Page<>();
        page.setCurrent(idRequest.getPage());
        page.setSize(idRequest.getLimit());

        LambdaQueryWrapper<Participation> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Participation::getUserId, idRequest.getId())
                .eq(Participation::getValid, true);

        page = participationMapper.selectPage(page, queryWrapper);

        CommonResponse<ParticipationResp> commonResponse = new CommonResponse<>();
        commonResponse.setTotal(page.getTotal());
        commonResponse.setCurrent(page.getCurrent());
        commonResponse.setSize(page.getSize());

        List<Participation> participationList = page.getRecords();
        List<ParticipationResp> participationRespList = new ArrayList<>();
        String userName = participationMapper.selectUserName(participationList.get(0).getUserId());

        for (Participation participation : participationList) {
            String activityName = activityMapper.selectById(participation.getActivityId()).getName();

            participationRespList.add(ParticipationResp.builder()
                    .id(participation.getId())
                    .userName(userName)
                    .activityName(activityName)
                    .participationTime(participation.getParticipateTime())
                    .ip(participation.getIp())
                    .isWinning(participation.getIsWinning())
                    .build()
            );
        }
        ;
        commonResponse.setData(participationRespList);
        return commonResponse;
    }

    @Override
    public boolean hasParticipated(Long userId, Integer activityId) {
        QueryWrapper<Participation> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId)
                .eq("activity_id", activityId)
                .eq("is_valid", true);

        System.out.println(participationMapper.selectCount(queryWrapper));


        return participationMapper.selectCount(queryWrapper) > 0;
    }
}