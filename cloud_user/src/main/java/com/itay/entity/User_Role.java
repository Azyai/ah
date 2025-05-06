package com.itay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_role")
public class User_Role {
    int id;
    int userId;
    int roleId;
}
