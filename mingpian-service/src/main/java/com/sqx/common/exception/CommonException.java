package com.sqx.common.exception;

import lombok.Data;

@Data//异常类
public class CommonException extends RuntimeException{
    private int code=500;
    private String msg;

    //对该异常类的构造方法进行补充，不写的化会默认只有一个无参构造
    public CommonException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CommonException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CommonException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CommonException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
