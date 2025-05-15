package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.mapper.ActivityMapper;
import com.itay.service.ActivityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

}
