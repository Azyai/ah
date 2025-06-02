package com.itay.controller;

import com.itay.apis.PriceApi;
import com.itay.entity.Prize;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/draw/prize")
public class PrizeConsumerController {

    @Autowired
    private PriceApi priceApi;

    @PostMapping("/selectPrize")
    ResultData<CommonResponse<Prize>> selectPrize(@RequestBody NameRequest nameRequest){
        return priceApi.selectPrize(nameRequest);
    }
}
