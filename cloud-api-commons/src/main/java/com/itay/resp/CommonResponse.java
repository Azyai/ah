package com.itay.resp;

import lombok.Data;

import java.util.List;

@Data
public class CommonResponse<T> {
   List<T> data;
   Long total;
   Long current;
   Long size;
}
