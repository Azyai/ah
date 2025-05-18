package com.itay.request;

import lombok.Data;

@Data
public class NameRequest extends BaseRequest {
    private String name;
}