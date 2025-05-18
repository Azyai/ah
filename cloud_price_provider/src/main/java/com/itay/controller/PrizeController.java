package com.itay.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itay.dto.request.NameRequest;
import com.itay.entity.Prize;
import com.itay.resp.ResultData;
import com.itay.service.PrizeService;
import jakarta.validation.Valid;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@RestController
@RequestMapping("/draw/prize")
public class PrizeController {

    @Autowired
    PrizeService prizeService;

    // 获取奖品表
    @GetMapping("/selectPrize")
    public ResultData<List<Prize>> selectPrize(NameRequest nameRequest) {
        List<Prize> prizes = prizeService.selectPrize(nameRequest);
        if(prizes.isEmpty()){
            return ResultData.fail("没有该奖品");
        }
        return ResultData.success(prizes);
    }

    // 增加奖品,使用@Validated对prize进行校验
    @PostMapping("/addPrize")
    public ResultData<String> addPrize(@RequestBody @Valid Prize prize){
        if(prizeService.save(prize)){
            return ResultData.success("添加奖品成功");
        }
        return ResultData.fail("添加奖品失败");
    }

    @PostMapping("/updatePrize")
    public ResultData<String> updatePrize(@Valid Prize prize){
        if(prizeService.updateById(prize)){
            return ResultData.success("修改奖品成功");
        }
        return ResultData.fail("修改奖品失败");
    }

    @PostMapping("/deletePrize")
    public ResultData<String> deletePrize(@Valid Prize prize){
        if(prizeService.removePrize(prize)){
            return ResultData.success("删除奖品成功");
        }
        return ResultData.fail("删除奖品失败");
    }

}