package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
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
public class Prize extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @NotBlank // 验证字段不能为空
    // 奖品名称
    private String name;
    
    @Builder.Default
    // 是否虚拟奖品
    private Boolean isVirtual = false;

    // 奖品配置
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> configTemplate;

    // 奖品描述
    private String description;
}