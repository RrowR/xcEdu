package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

public class CustomerException extends RuntimeException {
    //传入错误代码
    ResultCode resultCode;
    //编写有参构造方法，目的是初始化对象
        public CustomerException(ResultCode resultCode) {
//        将构造方法的resultcode传入给上面的resultcode属性中去
        this.resultCode = resultCode;
    }

    //拿到上面的错误代码
    public ResultCode getResultCode() {
        return resultCode;
    }
}
