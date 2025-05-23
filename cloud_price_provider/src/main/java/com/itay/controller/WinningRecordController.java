package com.itay.controller;

import com.itay.entity.resp.WinningRecordResp;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import com.itay.service.WinningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/draw/winningRecord")
public class WinningRecordController {

    @Autowired
    private WinningRecordService winningRecordService;

    @PostMapping("/getWinningRecord")
    public ResultData<CommonResponse<WinningRecordResp>> getWinningRecord(@RequestBody IdRequest idRequest){
        CommonResponse<WinningRecordResp> winningRecordResp = winningRecordService.selectWinningRecordResp(idRequest);
        return ResultData.success(winningRecordResp);
    }
}
