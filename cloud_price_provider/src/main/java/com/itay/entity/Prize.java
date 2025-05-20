package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.itay.pojo.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("prize")
public class Prize extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @NotBlank // 验证字段不能为空
    @TableField(value = "`name`")
    private String name;

    // 是否为虚拟奖品 0不是 1是
    @Builder.Default
    private Boolean isVirtual = false;

    // 奖品配置模板
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> configTemplate;

    // 描述
    private String description;
}