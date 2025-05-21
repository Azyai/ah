package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.WinningRecord;

/**
 * @author ly111
 */
public interface WinningRecordService extends IService<WinningRecord> {

    boolean addAndUpdateActivity(WinningRecord winningRecord);
}
