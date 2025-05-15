package com.itay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMQMessage extends BaseEntity{
    private String msgId;
    private MessageType type; // 枚举：COUNTER_UPDATE/PRIZE_SEND等
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;
    
    private Object body; // 根据type变化的消息体
    
    public enum MessageType {
        COUNTER_UPDATE, // 计数更新
        PRIZE_SEND,    // 奖品发放
        ACTIVITY_CLOSE // 活动关闭
    }
    
    // 计数更新专用消息体
    @Data
    public static class CounterUpdateBody {
        private Integer activityId;
        private Integer increment;
    }
}