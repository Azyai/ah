package com.itay.exception;


import com.itay.resp.ResultData;
import com.itay.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(RuntimeException.class)
     @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
     public ResultData<String> exception(Exception e){
         System.out.println("#####come in GlobalExceptionHandler");
         return ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
     }
}
