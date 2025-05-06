package com.itay.securityservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itay.entity.Role;
import com.itay.entity.RoleMenu;
import com.itay.entity.User;
import com.itay.entity.UserRole;
import com.itay.mapper.*;
import com.itay.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthorizeService implements UserDetailsService {

    @Resource
    private RbacService rbacService;

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1判断是否为空
        if(username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 2.获取用户的基本信息
        User user = userService.findByUsernameOrEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 3.获取用户的角色信息
        List<String> roleNames = rbacService.findRolesByUsername(user.getUsername());

        // 4.获取用户的权限信息
        List<String> authorities = rbacService.findAuthoritiesByRoleName(roleNames);

        // 5.告诉security角色有那些，角色的格式为：Role_xxx 所以这里需要修改一下
        roleNames = roleNames.stream().map(role -> "ROLE_" + role).toList();

        // 6.告诉security角色和权限:这里也是做了一个转换，因为setAuthorities需要的是GrantedAuthority
        //  而GrantedAuthority实现了Serializable，因此我们使用commaSeparatedStringToAuthorityList方法进行转换
        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",authorities)));

        System.out.println(user + "-----------------");
        return user;

    }
}
