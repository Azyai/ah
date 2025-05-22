package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.dto.response.ParticipationResp;
import com.itay.entity.Activity;
import com.itay.entity.Participation;
import com.itay.entity.Prize;
import com.itay.pojo.ActivityCounter;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;

import java.util.List;


/**
 * @author ly111
 */
public interface ParticipationService extends IService<Participation> {

    boolean addParticipate(Long userId,Integer activityId,String ip,String deviceFingerprint);

    boolean updateCounter(ActivityCounter counter, Activity activity);

    int getParticipateCount(Integer activityId);

    Prize drawPrize(Activity activity);

    CommonResponse<ParticipationResp> selectParticipationResp(IdRequest idRequest);
}
