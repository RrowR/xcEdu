package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResultCode;

//封装错误代码，这个静态方法可以直接调用，使用方便
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        //抛出被封装的异常
        throw new CustomerException(resultCode);
    }
}
