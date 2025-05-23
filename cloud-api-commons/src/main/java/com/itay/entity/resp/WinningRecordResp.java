package com.itay.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinningRecordResp {
    private String id;
    private String userName;
    private String activityName;
    private String prizeName;
    private LocalDateTime winningTime;
    private Integer status;
    private String mqMsgId;
    private String participationId;
}
