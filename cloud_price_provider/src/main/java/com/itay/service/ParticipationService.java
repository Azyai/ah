package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.Activity;
import com.itay.entity.Participation;
import com.itay.entity.resp.ParticipationResp;
import com.itay.entity.Prize;
import com.itay.pojo.ActivityCounter;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;


/**
 * @author ly111
 */
public interface ParticipationService extends IService<Participation> {

    boolean addParticipate(String participationId, Long userId, Integer activityId, String ip, String deviceFingerprint);

    void updateWinningStatus(String participationId, boolean isWinning);

    boolean updateCounter(ActivityCounter counter, Activity activity);

    int getParticipateCount(Integer activityId);

    Prize drawPrize(Activity activity);

    CommonResponse<ParticipationResp> selectParticipationResp(IdRequest idRequest);

    boolean hasParticipated(Long userId, Integer activityId);
}
