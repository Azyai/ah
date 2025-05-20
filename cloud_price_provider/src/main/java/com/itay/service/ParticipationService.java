package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.Participation;
import com.itay.resp.ResultData;


/**
 * @author ly111
 */
public interface ParticipationService extends IService<Participation> {

    boolean addParticipate(Long userId,Integer activityId,String ip,String deviceFingerprint);
}
