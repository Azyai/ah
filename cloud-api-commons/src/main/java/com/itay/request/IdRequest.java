package com.itay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IdRequest {
    private Long id;
    private Integer page = 1;

    private Integer limit = 10;
}
