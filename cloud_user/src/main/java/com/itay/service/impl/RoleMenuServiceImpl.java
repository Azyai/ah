package com.itay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.RoleMenu;
import com.itay.entity.UserRole;
import com.itay.mapper.RoleMenuMapper;
import com.itay.mapper.UserRoleMapper;
import com.itay.service.RoleMenuService;
import com.itay.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {


    @Override
    public List<String> findAuthoritiesByRoleName(List<String> roleNames) {
        return this.baseMapper.findAuthoritiesByRoleName(roleNames);
    }
}
