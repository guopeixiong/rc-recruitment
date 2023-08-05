package com.ruanchuang.filter;

import com.ruanchuang.handler.RequestWrapper;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @Author guopeixiong
 * @Date 2023/8/5
 * @Email peixiongguo@163.com
 */
public class RepeatableFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (requestWrapper instanceof HttpServletRequestWrapper && servletRequest.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest, servletResponse);
        }
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
}
