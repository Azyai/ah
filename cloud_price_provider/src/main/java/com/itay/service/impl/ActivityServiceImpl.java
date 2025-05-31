package com.itay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.dto.response.ActivityResp;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import com.itay.entity.Prize;
import com.itay.entity.resp.ActivityInfoResp;
import com.itay.entity.resp.ActivityPrizeResp;
import com.itay.entity.resp.ActivityRestrictionResp;
import com.itay.mapper.ActivityMapper;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.mapper.ActivityRestrictionMapper;
import com.itay.mapper.PrizeMapper;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.service.ActivityPrizeService;
import com.itay.service.ActivityRestrictionService;
import com.itay.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ly111
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Resource
    private ActivityPrizeService activityPrizeService;

    @Autowired
    private ActivityRestrictionService activityRestrictionService;

    // 还是要每分钟执行一次
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void autoUpdateActivityStatus(){
        LocalDateTime now = LocalDateTime.now();

        // 1. 更新应该开始但未开始的活动（状态1-》2）
        activityMapper.update(null,
                new LambdaUpdateWrapper<Activity>()
                        .eq(Activity::getStatus,1)
                        .le(Activity::getStartTime, now)
                        .set(Activity::getStatus,2)
        );

        // 2. 更新应该结束但未结束的活动（状态2-》3）
        activityMapper.update(null,
                new LambdaUpdateWrapper<Activity>()
                        .eq(Activity::getStatus,2)
                        .le(Activity::getEndTime, now)
                        .set(Activity::getStatus,3)
        );

    }


    @Override
    @Transactional
    public Boolean addActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction) {
        // 1.插入活动信息
        if (!this.save(activity)) {
            throw new RuntimeException("活动保存失败，事务将回滚");
        }

        // 2. 设置奖品的activityId
        Integer activityId = activity.getId();
        for (ActivityPrize prize : prizes) {
            prize.setActivityId(activityId);
        }

        // 3.批了插入奖品信息
        if (!activityPrizeService.saveBatch(prizes)) {
            throw new RuntimeException("奖品保存失败，事务将回滚");
        }

        // 4.插入活动限制信息
        activityRestriction.setActivityId(activityId);
        if (!activityRestrictionService.save(activityRestriction)) {
            throw new RuntimeException("活动限制保存失败，事务将回滚");
        }

        return true;
    }

    @Override
    @Transactional
    public boolean deleteBatchActivity(List<Integer> ids) {
        // 1.逻辑删除活动信息
        LambdaUpdateWrapper<Activity> activityWrapper = new LambdaUpdateWrapper<>();
        activityWrapper.in(Activity::getId, ids);
        activityWrapper.set(Activity::getValid, false);
        int update1 = activityMapper.update(null, activityWrapper);

        // 2.批量逻辑删除 活动奖品信息
        LambdaUpdateWrapper<ActivityPrize> prizeWrapper = new LambdaUpdateWrapper<>();
        prizeWrapper.in(ActivityPrize::getPrizeId, ids);
        prizeWrapper.set(ActivityPrize::getValid, false);
        int update2 = activityPrizeMapper.update(null, prizeWrapper);

        // 3.逻辑删除活动限制信息
        LambdaUpdateWrapper<ActivityRestriction> restrictionWrapper = new LambdaUpdateWrapper<>();
        restrictionWrapper.in(ActivityRestriction::getActivityId, ids);
        restrictionWrapper.set(ActivityRestriction::getValid, false);
        boolean update3 = activityRestrictionService.update(restrictionWrapper);

        return update1 > 0 && update2 > 0 && update3;
    }

    @Override
    @Transactional
    public boolean updateActivity(Activity activity, List<ActivityPrize> prizes, ActivityRestriction activityRestriction) {
        // 1.更新活动信息
        if (!this.updateById(activity)) {
            throw new RuntimeException("活动更新失败，事务将回滚");
        }

        // 2.更新奖品信息
        if(!activityPrizeService.updateBatchById(prizes)){
            throw new RuntimeException("奖品更新失败，事务将回滚");
        }

        // 3.更新活动限制信息
        if(!activityRestrictionService.updateById(activityRestriction)){
            throw new RuntimeException("活动限制更新失败，事务将回滚");
        }
        return true;
    }

    @Override
    @Transactional
    public CommonResponse<ActivityResp> selectActivity(String name, Integer page, Integer limit) {
        List<ActivityResp> activityResp = new ArrayList<>();

        IPage<Activity> ipage = new Page<>(page, limit);
        LambdaQueryWrapper<Activity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Activity::getName, name)
                .eq(Activity::getValid, true);

        // 1.查询出活动信息
        ipage = activityMapper.selectPage(ipage, lambdaQueryWrapper);
        List<Activity> activities = ipage.getRecords();
        List<ActivityPrize> activityPrizes;

        // 2.查询出活动奖品信息
        for (Activity activity : activities) {
            Integer activityId = activity.getId();
            activityPrizes = activityPrizeMapper.selectList(new LambdaQueryWrapper<ActivityPrize>()
                    .eq(ActivityPrize::getActivityId, activityId)
                    .eq(ActivityPrize::getValid, true));

            // 2.1查询出活动限制信息
            ActivityRestriction activityRestrictions = activityRestrictionService.getById(activityId);

            // 2.2组合返回
            activityResp.add(new ActivityResp(activity, activityPrizes, activityRestrictions));
        }

        // 3.构建分页返回对象
        CommonResponse<ActivityResp> commonResponse = new CommonResponse<>();
        commonResponse.setData(activityResp);
        commonResponse.setTotal(ipage.getTotal());
        commonResponse.setCurrent(ipage.getCurrent());
        commonResponse.setSize(ipage.getSize());
        return commonResponse;
    }

    @Override
    public CommonResponse<ActivityInfoResp> selectActivityInfo(String name, Integer page, Integer limit) {


        // 1. 获取对应活动名称的活动信息
        IPage<Activity> activityPage = new Page<>(page, limit);
        LambdaQueryWrapper<Activity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Activity::getName, name)
                .eq(Activity::getValid, true);

        // 2.查询活动的基本信息(分页)
        activityPage = activityMapper.selectPage(activityPage, lambdaQueryWrapper);
        List<Activity> activities = activityPage.getRecords();

        // 3.构建返回结果infoRespList
        List<ActivityInfoResp> infoRespList = activities.stream().map(activity -> {
            // 3.1查询活动奖品信息
            List<ActivityPrize> prizes = activityPrizeMapper.selectList(new LambdaQueryWrapper<ActivityPrize>()
                    .eq(ActivityPrize::getActivityId, activity.getId())
                    .eq(ActivityPrize::getValid, true));

            // 3.2查询活动限制信息
            ActivityRestriction restriction = activityRestrictionService.getById(activity.getId());

            // 3.3封装为ActivityInfoResp
            return ActivityInfoResp.builder()
                    .activity(activity)
                    .prizes(convertToPrizeResp(prizes))
                    .restriction(convertToRestrictionResp(restriction))
                    .build();
        }).collect(Collectors.toList());

        // 4.构建分页返回对象
        CommonResponse<ActivityInfoResp> response = new CommonResponse<>();
        response.setData(infoRespList);
        response.setTotal(activityPage.getTotal());
        response.setCurrent(activityPage.getCurrent());
        response.setSize(activityPage.getSize());
        return response;
    }

    @Override
    public ActivityInfoResp fetchActivityDetailById(Integer id) {
        // 1.按照id获取单个活动信息，然后将活动奖品和限制也返回
        Activity activity = activityMapper.selectById(id);

        return ActivityInfoResp.builder()
                .activity(activity)
                .prizes(convertToPrizeResp(activityPrizeMapper.selectList(new LambdaQueryWrapper<ActivityPrize>()
                        .eq(ActivityPrize::getActivityId, id)
                        .eq(ActivityPrize::getValid, true))))
                .restriction(convertToRestrictionResp(activityRestrictionService.getById(id)))
                .build();
    }

    // 转换奖品信息为Resp格式
    private List<ActivityPrizeResp> convertToPrizeResp(List<ActivityPrize> prizes) {
        return prizes.stream().map(prize -> {
            // 1. 根据prizeId查询奖品名称
            Prize prizeDetail = prizeMapper.selectById(prize.getPrizeId());
            String prizeName = prizeDetail != null ? prizeDetail.getName() : "未知奖品";

            // 2. 构建ActivityPrizeResp
            return ActivityPrizeResp.builder()
                    .id(prize.getId())
                    .prizeName(prizeName)
                    .totalStock(prize.getTotalStock().toString())
                    .usedStock(prize.getUsedStock())
                    .probability(prize.getProbability() != null ?
                            prize.getProbability().doubleValue() : 0.0)
                    .build();
        }).collect(Collectors.toList());
    }

    // 转换限制信息为Resp格式
    private ActivityRestrictionResp convertToRestrictionResp(ActivityRestriction restriction) {
        if (restriction == null) {
            return ActivityRestrictionResp.builder().build();
        }
        return ActivityRestrictionResp.builder()
                .activityId(restriction.getActivityId())
                .limitType(restriction.getLimitType())
                .limitValue(restriction.getLimitValue())
                .intervalSeconds(restriction.getIntervalSeconds())
                .build();
    }


}
