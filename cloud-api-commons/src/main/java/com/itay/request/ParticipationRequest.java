package com.itay.request;

import lombok.Data;

@Data
public class ParticipationRequest {

    private Long userId;

    private Integer activityId;

    private String ip;

    private String deviceFingerprint;
}
