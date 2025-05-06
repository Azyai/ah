package com.itay.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Role {
    private Long id;
    private String name;
    private String description;

}
