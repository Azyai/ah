package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.Prize;
import com.itay.request.NameRequest;

import java.util.List;

/**
 * @author ly111
 */
public interface PrizeService extends IService<Prize> {
    List<Prize> selectPrize(NameRequest nameRequest);

    boolean removePrize(Integer id);
}
