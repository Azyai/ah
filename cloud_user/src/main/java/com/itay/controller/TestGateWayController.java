package com.itay.controller;

import com.itay.resp.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class TestGateWayController {

    @GetMapping("/test")
    public ResultData<String> test() {
        return ResultData.success("test");
    }

}
