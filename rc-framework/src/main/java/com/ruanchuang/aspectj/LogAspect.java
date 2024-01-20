package com.ruanchuang.aspectj;

import cn.hutool.core.bean.BeanUtil;
import com.ruanchuang.annotation.Log;
import com.ruanchuang.domain.SysLog;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.enums.BusinessStatus;
import com.ruanchuang.enums.HttpMethod;
import com.ruanchuang.handler.RequestContextHandler;
import com.ruanchuang.service.SysLogService;
import com.ruanchuang.utils.JSONUtils;
import com.ruanchuang.utils.LoginUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 日志处理
 *
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
@Aspect
@Component
public class LogAspect {

    @Resource(name = "systemThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private SysLogService sysLogService;

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    public void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        SysLog log = new SysLog();
        String token = RequestContextHandler.getUserToken();
        SysUser user = null;
        if (token != null) {
            user = LoginUtils.getLoginUser(token);
        }
        if (user != null) {
            log.setCreateBy(user.getFullName());
            log.setOperType(controllerLog.businessType());
        }
        log.setStatus(BusinessStatus.SUCCESS)
                .setRequestIp(getIpAddress(joinPoint));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestURI = attributes.getRequest().getRequestURI();
        log.setRequestUrl(requestURI);
        if (e != null) {
            log.setErrorMsg(e.getMessage().length() >= 2000 ? e.getMessage().substring(0, 2000) : e.getMessage());
            log.setStatus(BusinessStatus.FAIL);
        }
        log.setMethod(joinPoint.getSignature().getName())
                .setRequestMethod(attributes.getRequest().getMethod());
        getControllerMethodDescription(joinPoint, controllerLog, log, jsonResult);
        if (e != null) {
            logger.error(JSONUtils.toJsonString(log));
        } else {
            logger.info(JSONUtils.toJsonString(log));
        }
        log.setType(controllerLog.type());
        threadPoolTaskExecutor.execute(() -> sysLogService.save(log));
    }

    private void getControllerMethodDescription(JoinPoint joinPoint, Log controllerLog, SysLog log, Object jsonResult) {
        log.setOperType(controllerLog.businessType())
                .setTitle(controllerLog.title());
        if (controllerLog.saveRequestParam()) {
            setRequestValue(joinPoint, log);
        }
        if (controllerLog.saveResponseResult()) {
            log.setResponseResult(StringUtils.substring(JSONUtils.toJsonString(jsonResult), 0, 2000));
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysLog operLog) {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setRequestParam(StringUtils.substring(params, 0, 2000));
        } else {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Map<String, String[]> paramsMap = attributes.getRequest().getParameterMap();
            operLog.setRequestParam(StringUtils.substring(JSONUtils.toJsonString(paramsMap), 0, 2000));
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (BeanUtil.isNotEmpty(o)) {
                    String jsonObj = JSONUtils.toJsonString(o);
                    params += jsonObj + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 获取ip地址
     *
     * @param joinPoint
     * @return
     */
    private String getIpAddress(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                String ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
                return ipAddress;
            }
        }
        return null;
    }
}
