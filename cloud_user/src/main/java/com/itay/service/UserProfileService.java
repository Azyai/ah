package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.UserProfile;
import com.itay.resp.UserInfo;

public interface UserProfileService extends IService<UserProfile> {

    UserInfo findUserProfileByUserByUserId(Integer userId);

}
