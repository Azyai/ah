package com.itay.controller;

import com.itay.apis.PriceApi;
import com.itay.entity.resp.ActivityInfoResp;
import com.itay.request.CreateActivityRequest;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draw/activity")
public class ActivityConsumerController {

    @Autowired
    PriceApi priceApi;

    @PostMapping("/selectActivityInfo")
    public ResultData<CommonResponse<ActivityInfoResp>> selectActivityInfo(@RequestBody NameRequest nameRequest) {
        ResultData<CommonResponse<ActivityInfoResp>> commonResponseResultData = priceApi.selectActivityInfo(nameRequest);
        return commonResponseResultData;
    }

    @GetMapping("/fetchActivityDetailById")
    ResultData<ActivityInfoResp> fetchActivityDetailById(@RequestParam("id") Integer id){
        return priceApi.fetchActivityDetailById(id);
    }

    // 添加活动信息
    @PostMapping("/addActivity")
    ResultData<String> addActivity(@RequestBody CreateActivityRequest request){
        return priceApi.addActivity(request);
    }


}
