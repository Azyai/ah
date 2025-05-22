package com.itay.dto.response;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ParticipationResp {

    private Long id;
    private String userName;
    private String activityName;

    // 参与时间
    private LocalDateTime participationTime;

    // 参与该活动时的ip地址
    private String ip;

    // 是否中奖
    private Boolean isWinning;

}
