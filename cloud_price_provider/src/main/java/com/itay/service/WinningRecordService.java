package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.WinningRecord;
import com.itay.entity.resp.WinningRecordResp;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;

/**
 * @author ly111
 */
public interface WinningRecordService extends IService<WinningRecord> {

    boolean addAndUpdateActivity(WinningRecord winningRecord);

    CommonResponse<WinningRecordResp> selectWinningRecordResp(IdRequest idRequest);
}
