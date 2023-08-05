package com.ruanchuang.model;

import com.ruanchuang.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口统一响应结构
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SUCCESS = "操作成功";

    private static final String FAIL = "操作失败";

    private int code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> ok() {
        return new CommonResult<>(HttpStatus.SUCCESS, SUCCESS, null);
    }

    public static <T> CommonResult<T> ok(T data, String message) {
        return new CommonResult<>(HttpStatus.SUCCESS, message, data);
    }

    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<>(HttpStatus.SUCCESS, SUCCESS, data);
    }

    public static <T> CommonResult<T> fail(String message, int code) {
        return new CommonResult<>(code, message, null);
    }

    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<>(HttpStatus.ERROR, message, null);
    }

    public static <T> CommonResult<T> fail() {
        return new CommonResult<>(HttpStatus.ERROR, FAIL, null);
    }

}
