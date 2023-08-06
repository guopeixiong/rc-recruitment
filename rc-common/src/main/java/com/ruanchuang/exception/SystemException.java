package com.ruanchuang.exception;

/**
 * 系统异常类
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
public class SystemException extends RuntimeException {

    private String message;

    public SystemException() {}

    public SystemException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
