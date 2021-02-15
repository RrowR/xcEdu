package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


//这个是一个注解增强,其作用就是在抛出这个异常的时候，会自动调用这个方法
@ControllerAdvice
public class ExceptionCatch {
    //日志记录类
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerException.class);

    //统一捕获CustomerException.class异常
    @ExceptionHandler(CustomerException.class)
    public ResponseResult customerException(CustomerException customerException){
        //调用日志logger
        LOGGER.error("catch Exception:{}",customerException.getMessage());
        ResultCode resultCode = customerException.getResultCode();
        return new ResponseResult(resultCode);
    }
}
