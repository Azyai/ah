package com.itay.request;

import lombok.Data;

@Data
public class BaseRequest {
    private Integer page = 1;

    private Integer limit = 10;
}
