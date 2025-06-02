package com.itay.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itay.entity.Prize;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;
import com.itay.resp.ResultData;
import com.itay.resp.ReturnCodeEnum;
import com.itay.service.PrizeService;
import jakarta.validation.Valid;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/selectPrize")
    public ResultData<CommonResponse<Prize>> selectPrize(@RequestBody NameRequest nameRequest) {
        CommonResponse<Prize> prizes = prizeService.selectPrize(nameRequest);
        if (prizes.getData() == null || prizes.getData().isEmpty()) {
            return ResultData.fail(ReturnCodeEnum.RC404.getCode(), "没有符合条件的奖品");
        }
        return ResultData.success(prizes);
    }

    // 增加奖品,使用@Validated对prize进行校验
    @PostMapping("/addPrize")
    public ResultData<String> addPrize(@RequestBody @Valid Prize prize){
        if(prizeService.save(prize)){
            return ResultData.success("添加奖品成功");
        }
        return ResultData.fail("401","添加奖品失败");
    }

    @PostMapping("/updatePrize")
    public ResultData<String> updatePrize(@RequestBody @Valid Prize prize){
        if(prizeService.updateById(prize)){
            return ResultData.success("修改奖品成功");
        }
        return ResultData.fail("401","修改奖品失败");
    }

    @PostMapping("/deletePrize")
    public ResultData<String> deletePrize(@RequestParam("id") Integer id){
        if(prizeService.removePrize(id)){
            return ResultData.success("删除奖品成功");
        }
        return ResultData.fail("401","删除奖品失败");
    }

}