package com.itay.controller;

import com.itay.resp.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getServerPort")
    public ResultData<String> getServerPort() {
        return ResultData.success("nacos openfeign:" + serverPort);
    }

}