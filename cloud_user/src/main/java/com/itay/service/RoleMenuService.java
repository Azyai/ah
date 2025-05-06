package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.RoleMenu;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {
 List<String> findAuthoritiesByRoleName(List<String> roleNames);
}
