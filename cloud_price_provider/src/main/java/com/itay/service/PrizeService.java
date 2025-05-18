package com.itay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itay.entity.Prize;
import com.itay.request.NameRequest;
import com.itay.resp.CommonResponse;


/**
 * @author ly111
 */
public interface PrizeService extends IService<Prize> {
    CommonResponse<Prize> selectPrize(NameRequest nameRequest);

    boolean removePrize(Integer id);
}
