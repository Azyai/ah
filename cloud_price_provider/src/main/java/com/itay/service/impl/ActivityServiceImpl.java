package com.itay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.mapper.ActivityMapper;
import com.itay.request.NameRequest;
import com.itay.service.ActivityPrizeService;
import com.itay.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author ly111
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Override
    public List<Activity> getActivityByNameList(String name) {
        // 按照名称模糊查询构造
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Activity::getName, name);
        queryWrapper.eq(Activity::getValid, true);

        return activityMapper.selectList(queryWrapper);
    }

    @Override
    public Activity getActivityById(Integer id) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getId, id);
        queryWrapper.eq(Activity::getValid, true);

        return activityMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean removeActivityById(Integer id) {
        LambdaUpdateWrapper<Activity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Activity::getId, id);

        // 修改字段is_valid逻辑删除
        lambdaUpdateWrapper.set(Activity::getValid, false);
        int update = activityMapper.update(null, lambdaUpdateWrapper);

        return update > 0;
    }

    @Override
    public boolean removeActivityByIds(List<Integer> ids) {
        //  批量逻辑删除
        LambdaUpdateWrapper<Activity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Activity::getId, ids);
        lambdaUpdateWrapper.set(Activity::getValid, false);
        int update = activityMapper.update(null, lambdaUpdateWrapper);
        return update > 0;
    }

    @Override
    public List<Activity> selectActivityPageByName(NameRequest prizeRequest) {
        IPage<Activity> page = new Page<>(prizeRequest.getPage(), prizeRequest.getLimit());
        LambdaQueryWrapper<Activity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Activity::getName, prizeRequest.getName())
                .eq(Activity::getValid, true);
        page = activityMapper.selectPage(page, lambdaQueryWrapper);

        System.out.println(page.toString());
        return page.getRecords();
    }


    @Resource
    private ActivityPrizeService activityPrizeService;

    @Override
    /**
     * 使用 @Transactional 注解来确保数据库操作的事务性。
     * 该注解是 Spring 提供的，作用是将当前方法包装成一个数据库事务。
     *
     * 具体原理：
     * 1. 当调用此方法时，Spring 会自动开启一个新的事务。
     * 2. 如果方法执行过程中没有发生异常，事务会在方法成功完成后提交。
     * 3. 如果方法中抛出运行时异常（RuntimeException）或指定回滚的检查异常，Spring 会标记事务回滚，所有更改不会写入数据库。
     * 4. 此外，@Transactional 还支持传播行为、隔离级别和超时等高级配置，默认情况下使用的是 Propagation.REQUIRED 和数据库默认隔离级别。
     *
     * 在这个方法中添加此注解是为了保证保存活动信息和奖品信息这两个操作在同一个事务中完成，
     * 避免因为其中一个操作失败而导致数据不一致的问题。
     */
    @Transactional
    public boolean saveActivityWithPrizes(Activity activity, List<ActivityPrize> prizes) {
        // 1.插入活动信息
        if (!this.save(activity)) {
            return false;
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

        return true;
    }
}
