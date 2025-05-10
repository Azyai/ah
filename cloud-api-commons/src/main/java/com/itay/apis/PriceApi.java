package com.itay.apis;

import com.itay.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("cloud-price")
public interface PriceApi {

    @GetMapping(value = "/getServerPort")
    // 理论上抽象方法名可以随便起，但是最好与业务实现代码保持一致
    // 起决定作用的主要是URL路径
    public ResultData<String> getServerPort();


}