package com.itay.apis;

import com.itay.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cloud-price-provider")
@RequestMapping("/draw/participation")
public interface ParticipationApi {

    @PostMapping("/participate")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    public ResultData<String> participate(
            @RequestParam Long userId,
            @RequestParam Integer activityId,
            @RequestParam String ip,
            @RequestParam String deviceFingerprint
    );


}