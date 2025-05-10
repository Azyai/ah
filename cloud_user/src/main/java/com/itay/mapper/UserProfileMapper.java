package com.itay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.entity.User;
import com.itay.entity.UserProfile;
import com.itay.resp.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    UserInfo findUserProfileByUserByUserId(String userId);

}
