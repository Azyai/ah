package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.UserRole;
import com.itay.mapper.UserRoleMapper;
import com.itay.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Override
    public List<String> findRolesByUsername(String userName) {
        return this.baseMapper.findRolesByUsername(userName);
    }

}
