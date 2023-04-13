package com.wangzf.model.result;

import lombok.Data;
//springboot自带的jackson，直接返回的类必须要有get,set方法
@Data
public class Result<T> {

    private Integer code;

    private String message;
    private T data;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(){}
}
