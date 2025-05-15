package com.itay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Participation extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Integer activityId;
    
    @Builder.Default
    private LocalDateTime participateTime = LocalDateTime.now();
    
    @NotBlank
    private String ip;
    
    private String deviceFingerprint;
    
    @Builder.Default
    private Boolean isWinning = false;
}