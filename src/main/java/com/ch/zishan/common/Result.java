package com.ch.zishan.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
public class Result<T> {
    private String  code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据
    private Map map = new HashMap(); //动态数据

     public static <T> Result<T> success(T object) {
       Result<T> result = new Result<T>();
        result.data = object;
        result.code = "200";
        return result;
    }

    public static <T> Result<T> success(T object, String msg) {
        Result result = new Result();
        result.code = "200";
        result.data = object;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> success(String code,String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = "404";
        return result;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public static <T> Result<T> error(String code, String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }
}