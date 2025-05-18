package com.itay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itay.entity.Prize;
import com.itay.mapper.PrizeMapper;
import com.itay.request.NameRequest;
import com.itay.service.PrizeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize> implements PrizeService {

    @Resource
    PrizeMapper prizeMapper;

    @Override
    public List<Prize> selectPrize(NameRequest nameRequest) {
        IPage<Prize> page = new Page<>(nameRequest.getPage(), nameRequest.getLimit());
        LambdaQueryWrapper<Prize> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Prize::getName, nameRequest.getName())
                .eq(Prize::getValid, true);

        page = prizeMapper.selectPage(page, lambdaQueryWrapper);
        return page.getRecords();
    }

    @Override
    public boolean removePrize(Integer id) {
        LambdaUpdateWrapper<Prize> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Prize::getId, id);
        lambdaUpdateWrapper.set(Prize::getValid, false);

        return prizeMapper.update(null, lambdaUpdateWrapper) > 0;
    }
}