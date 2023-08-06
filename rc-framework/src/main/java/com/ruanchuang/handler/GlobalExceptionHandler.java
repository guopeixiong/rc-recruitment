package com.ruanchuang.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.ruanchuang.constant.HttpStatus;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.model.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(NotLoginException.class)
    public CommonResult handlerNotLoginException(NotLoginException nle, HttpServletRequest request) {

        log.error("未登录异常, 请求地址'{}', 异常信息'{}'", request.getRequestURI(), nle.getLocalizedMessage());
        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        } else {
            message = "当前会话未登录";
        }
        // 返回给前端
        return CommonResult.fail(message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return CommonResult.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public CommonResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error("业务异常, 请求地址'{}', 异常信息'{}'", request.getRequestURI(), e.getMessage());
        Integer code = e.getCode();
        return code != null ? CommonResult.fail(e.getMessage(), code) : CommonResult.fail(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常, 异常信息'{}'.", requestURI, e.getMessage());
        return CommonResult.fail("系统异常");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常, 异常信息'{}'", requestURI, e.getMessage());
        return CommonResult.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public CommonResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return CommonResult.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return CommonResult.fail(message);
    }

    /**
     * 拦截参数校验不通过异常
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Object handleConstraintViolationException(ConstraintViolationException e) {
        Optional<ConstraintViolation<?>> first = e.getConstraintViolations().stream().findFirst();
        log.error(e.getMessage(), e);
        return CommonResult.fail(first.isPresent() ? first.get().getMessage() : "参数不合法");
    }

}
