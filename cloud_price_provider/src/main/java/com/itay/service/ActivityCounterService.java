package com.itay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActivityCounterService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;



}
