package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itay.pojo.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("participation")
public class Participation extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Integer activityId;
    
    @Builder.Default
    private LocalDateTime participateTime = LocalDateTime.now();
    
    @NotBlank
    // IP地址，获取方式为：获取用户请求的IP地址
    private String ip;

    // 设备指纹，获取方式为：获取用户代理字符串，然后调用第三方库生成设备指纹
    private String deviceFingerprint;
    
    @Builder.Default
    private Boolean isWinning = false;
}