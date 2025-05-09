package com.itay.handler;

import com.itay.resp.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class QXGlobalExceptionHandler {



    @ExceptionHandler(AccessDeniedException.class)
    public ResultData<String> handleAccessDenied(AccessDeniedException e) {
        System.out.println("权限不足"+ e.getMessage());
        return ResultData.fail("401","没有权限执行此操作");
    }
}