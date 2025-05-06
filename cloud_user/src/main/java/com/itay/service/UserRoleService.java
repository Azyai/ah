package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    List<String> findRolesByUsername(String userName);
}
