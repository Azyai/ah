package com.itay.controller;

import com.itay.apis.PriceApi;
import com.itay.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private PriceApi priceApi;

    @GetMapping("/consumer/getServerPort")
    public ResultData<String> getServerPort() {
        return priceApi.getServerPort();
    }

}