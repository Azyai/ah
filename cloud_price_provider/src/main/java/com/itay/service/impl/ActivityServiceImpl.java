package com.itay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.mapper.ActivityMapper;
import com.itay.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
