package com.itay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUsernameOrEmail(String text);

    @Insert("insert into user(username, password, email,enabled,accountNonExpired,accountNonLocked,credentialsNonExpired) values(#{username},#{password},#{email},1,1,1,1)")
    int createAccount(String username, String password,String email);

}
