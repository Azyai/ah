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
public class Prize {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @NotBlank // 验证字段不能为空
    private String name;
    
    @Builder.Default
    private Boolean isVirtual = false;
    
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> configTemplate;
    
    private String description;
}