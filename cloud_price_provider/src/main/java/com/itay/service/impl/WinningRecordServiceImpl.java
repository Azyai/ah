package com.itay.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.WinningRecord;
import com.itay.entity.resp.WinningRecordResp;
import com.itay.mapper.ActivityMapper;
import com.itay.mapper.ActivityPrizeMapper;
import com.itay.mapper.PrizeMapper;
import com.itay.mapper.WinningRecordMapper;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;
import com.itay.service.WinningRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WinningRecordServiceImpl extends ServiceImpl<WinningRecordMapper, WinningRecord> implements WinningRecordService {

    @Resource
    private WinningRecordMapper winningRecordMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    PrizeMapper prizeMapper;

    @Resource
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    @Transactional
    public boolean addAndUpdateActivity(WinningRecord winningRecord) {
        // 1.中奖之后要存储中奖信息,
        int insert = winningRecordMapper.insert(winningRecord);
        if (insert < 0){
            throw new RuntimeException("存储中奖信息失败");
        }

        // 2.更改activity中参与人数，如果活动人数达到上限，则结束活动
        Activity activity = activityMapper.selectById(winningRecord.getActivityId());
        if(activity.getCurrentParticipants() + 1 <= activity.getMaxParticipants()){
            activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
            int update = activityMapper.updateById(activity);
            if(update < 0){
                throw new RuntimeException("活动人数更新失败");
            }
        }else {
            activity.setStatus(2);
            int update = activityMapper.updateById(activity);
            if(update < 0){
                throw new RuntimeException("活动状态更新失败");
            }
        }

        // 3.更改activityPrize中的奖品数量，如果所有的奖品数量不够，则结束活动
        ActivityPrize prize = activityPrizeMapper.selectById(winningRecord.getPrizeId());
        if(prize.getUsedStock() + 1 <= prize.getTotalStock()){
            prize.setUsedStock(prize.getUsedStock() + 1);
            int update = activityPrizeMapper.updateById(prize);
            if(update < 0){
                throw new RuntimeException("奖品数量更新失败");
            }
        }

        return true;
    }

    @Override
    public CommonResponse<WinningRecordResp> selectWinningRecordResp(IdRequest idRequest) {
        IPage<WinningRecord> page = new Page<>();
        LambdaQueryWrapper<WinningRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WinningRecord::getUserId,idRequest.getId())
                .eq(WinningRecord::getValid,true);

        System.out.println("idRequest: " + idRequest);
        page = winningRecordMapper.selectPage(page,queryWrapper);

        System.out.println("page: " + page);

        CommonResponse<WinningRecordResp> commonResponse = new CommonResponse<>();
        commonResponse.setTotal(page.getTotal());
        commonResponse.setSize(page.getSize());
        commonResponse.setCurrent(page.getCurrent());

        List<WinningRecord> recordList = page.getRecords();
        List<WinningRecordResp> recordRespList = new ArrayList<>();

        String userName = winningRecordMapper.selectUserName(recordList.get(0).getUserId());
        for (WinningRecord record : recordList) {
            String activityName = activityMapper.selectById(record.getActivityId()).getName();
            String prizeName = prizeMapper.selectById(record.getPrizeId()).getName();

            recordRespList.add(WinningRecordResp.builder()
                    .id(record.getId())
                    .userName(userName)
                    .activityName(activityName)
                    .prizeName(prizeName)
                    .winningTime(record.getWinTime())
                    .status(record.getStatus())
                    .mqMsgId(record.getMqMsgId())
                    .participationId(record.getParticipationId())
                    .build());
        }

        commonResponse.setData(recordRespList);
        return commonResponse;
    }
}