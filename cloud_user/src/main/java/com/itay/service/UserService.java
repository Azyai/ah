package com.itay.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.User;

public interface UserService extends IService<User> {
    User findByUsernameOrEmail(String username);
}
