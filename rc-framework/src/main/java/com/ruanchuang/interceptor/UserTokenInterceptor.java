package com.ruanchuang.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.ruanchuang.handler.RequestContextHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author guopeixiong
 * @Date 2023/8/3
 * @Email peixiongguo@163.com
 */
public class UserTokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StpUtil.isLogin()) {
            String token = StpUtil.getTokenValue();
            RequestContextHandler.setUserToken(token);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

}
