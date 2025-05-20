package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.UserProfile;
import com.itay.mapper.UserProfileMapper;
import com.itay.resp.UserInfo;
import com.itay.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper,UserProfile> implements UserProfileService {

    @Autowired
    UserProfileMapper userProfileMapper;

    @Override
    public UserInfo findUserProfileByUserByUserNameOreMail(String userId) {
        UserInfo userInfo = userProfileMapper.findUserProfileByUserByUserId(userId);

        return userInfo;
    }
}
