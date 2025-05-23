package com.itay.apis;

import com.itay.entity.Activity;
import com.itay.entity.resp.ParticipationResp;
import com.itay.entity.resp.WinningRecordResp;
import com.itay.request.IdRequest;
import com.itay.request.ParticipationRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-price-provider")
public interface PriceApi {

    @GetMapping(value = "/draw/participation/getServerPort")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    public ResultData<String> getServerPort();


    @PostMapping("/draw/participation/participate")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    ResultData<String> participate(@RequestBody ParticipationRequest participationRequest);


    @GetMapping("/draw/participation/getActivityById")
    ResultData<Activity> getActivityById(@RequestParam("activityId")Integer activityId);

    @PostMapping("/draw/participation/getParticipation")
    // 查询肯定是分页查询，可以使用post来查，一会测试一下活动列表，使用名称已查询时在consumer端拆开
    ResultData<CommonResponse<ParticipationResp>> getParticipation(@RequestBody IdRequest idRequest);


    @PostMapping("/draw/winningRecord/getWinningRecord")
    ResultData<CommonResponse<WinningRecordResp>> getWinningRecord(@RequestBody IdRequest idRequest);
}