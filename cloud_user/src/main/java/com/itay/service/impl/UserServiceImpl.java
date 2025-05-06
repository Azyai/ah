package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.User;
import com.itay.mapper.UserMapper;
import com.itay.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsernameOrEmail(String username) {
        return this.baseMapper.findByUsernameOrEmail(username);
    }
}
