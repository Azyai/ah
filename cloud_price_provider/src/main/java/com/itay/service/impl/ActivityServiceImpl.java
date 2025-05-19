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
import com.itay.mapper.ActivityMapper;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.mapper.ActivityRestrictionMapper;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.service.ActivityPrizeService;
import com.itay.service.ActivityRestrictionService;
import com.itay.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ActivityPrizeMapper activityPrizeMapper;

//    @Override
//    public List<Activity> getActivityByNameList(String name) {
//        // 按照名称模糊查询构造
//        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(Activity::getName, name);
//        queryWrapper.eq(Activity::getValid, true);
//
//        return activityMapper.selectList(queryWrapper);
//    }
//
//    @Override
//    public Activity getActivityById(Integer id) {
//        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Activity::getId, id);
//        queryWrapper.eq(Activity::getValid, true);
//
//        return activityMapper.selectOne(queryWrapper);
//    }
//
//    @Override
//    public boolean removeActivityById(Integer id) {
//        LambdaUpdateWrapper<Activity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        lambdaUpdateWrapper.eq(Activity::getId, id);
//
//        // 修改字段is_valid逻辑删除
//        lambdaUpdateWrapper.set(Activity::getValid, false);
//        int update = activityMapper.update(null, lambdaUpdateWrapper);
//
//        return update > 0;
//    }
//
//    @Override
//    public boolean removeActivityByIds(List<Integer> ids) {
//        //  批量逻辑删除
//        LambdaUpdateWrapper<Activity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        lambdaUpdateWrapper.in(Activity::getId, ids);
//        lambdaUpdateWrapper.set(Activity::getValid, false);
//        int update = activityMapper.update(null, lambdaUpdateWrapper);
//        return update > 0;
//    }
//
//    @Override
//    public CommonResponse<Activity> selectActivityPageByName(NameRequest prizeRequest) {
//        IPage<Activity> page = new Page<>(prizeRequest.getPage(), prizeRequest.getLimit());
//        LambdaQueryWrapper<Activity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(Activity::getName, prizeRequest.getName())
//                .eq(Activity::getValid, true);
//        page = activityMapper.selectPage(page, lambdaQueryWrapper);
//        CommonResponse<Activity> commonResponse = new CommonResponse<>();
//        commonResponse.setTotal(page.getTotal());
//        commonResponse.setCurrent(page.getCurrent());
//        commonResponse.setData(page.getRecords());
//
//        return commonResponse;
//    }
//
//    @Override
//    /**
//     * 使用 @Transactional 注解来确保数据库操作的事务性。
//     * 该注解是 Spring 提供的，作用是将当前方法包装成一个数据库事务。
//     *
//     * 具体原理：
//     * 1. 当调用此方法时，Spring 会自动开启一个新的事务。
//     * 2. 如果方法执行过程中没有发生异常，事务会在方法成功完成后提交。
//     * 3. 如果方法中抛出运行时异常（RuntimeException）或指定回滚的检查异常，Spring 会标记事务回滚，所有更改不会写入数据库。
//     * 4. 此外，@Transactional 还支持传播行为、隔离级别和超时等高级配置，默认情况下使用的是 Propagation.REQUIRED 和数据库默认隔离级别。
//     *
//     * 在这个方法中添加此注解是为了保证保存活动信息和奖品信息这两个操作在同一个事务中完成，
//     * 避免因为其中一个操作失败而导致数据不一致的问题。
//     */
//    @Transactional
//    public boolean saveActivityWithPrizes(Activity activity, List<ActivityPrize> prizes) {
//        // 1.插入活动信息
//        if (!this.save(activity)) {
//            return false;
//        }
//
//        // 2. 设置奖品的activityId
//        Integer activityId = activity.getId();
//        for (ActivityPrize prize : prizes) {
//            prize.setActivityId(activityId);
//        }
//
//        // 3.批了插入奖品信息
//        if (!activityPrizeService.saveBatch(prizes)) {
//            throw new RuntimeException("奖品保存失败，事务将回滚");
//        }
//
//        return true;
//    }


    @Resource
    private ActivityPrizeService activityPrizeService;

    @Autowired
    private ActivityRestrictionService activityRestrictionService;

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
        return commonResponse;
    }


}
