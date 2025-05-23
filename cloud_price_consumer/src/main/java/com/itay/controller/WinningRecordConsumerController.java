package com.itay.controller;

import com.itay.apis.PriceApi;
import com.itay.apis.UserApi;
import com.itay.entity.resp.ParticipationResp;
import com.itay.entity.resp.WinningRecordResp;
import com.itay.request.IdRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/draw/winningRecord")
public class WinningRecordConsumerController {

    @Autowired
    private UserApi userApi;

    @Autowired
    private PriceApi participateApi;

    @RequestMapping("/getWinningRecord")
    public  ResultData<CommonResponse<WinningRecordResp>> getWinningRecord(@RequestBody IdRequest idRequest, HttpServletRequest httpServletRequest) {

        String username = httpServletRequest.getHeader("X-User");
        Long userId = userApi.getUserId(username);
        idRequest.setId(userId);

        return participateApi.getWinningRecord(idRequest);
    }
}
