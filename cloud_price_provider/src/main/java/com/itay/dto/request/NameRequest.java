package com.itay.dto.request;

import com.itay.request.BaseRequest;
import lombok.Data;

@Data
public class NameRequest extends BaseRequest {
    private String name;
}