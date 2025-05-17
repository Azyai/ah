package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.ActivityPrize;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.service.ActivityPrizeService;
import org.springframework.stereotype.Service;

@Service
public class ActivityPrizeServiceImpl extends ServiceImpl<ActivityPrizeMapper, ActivityPrize> implements ActivityPrizeService {
}
