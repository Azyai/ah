package com.itay.apis;

import com.itay.entity.Activity;
import com.itay.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-price-provider",path = "/draw/participation")
public interface PriceApi {

    @GetMapping(value = "/getServerPort")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    public ResultData<String> getServerPort();


    @PostMapping("/participate")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    public ResultData<String> participate(
            @RequestParam("userId") Long userId,
            @RequestParam("activityId") Integer activityId,
            @RequestParam("ip") String ip,
            @RequestParam("deviceFingerprint") String deviceFingerprint
    );


    @GetMapping("/getActivityById")
    ResultData<Activity> getActivityById(@RequestParam("activityId")Integer activityId);

}