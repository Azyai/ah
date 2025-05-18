package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.dto.request.NameRequest;
import com.itay.entity.Prize;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author ly111
 */
public interface PrizeService extends IService<Prize> {
    List<Prize> selectPrize(NameRequest nameRequest);

    boolean removePrize(@Valid Prize prize);
}
