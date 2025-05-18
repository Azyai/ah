package com.itay.dto.request;

import com.itay.request.BaseRequest;
import lombok.Data;

@Data
public class PrizeRequest extends BaseRequest {
    private String name;
}