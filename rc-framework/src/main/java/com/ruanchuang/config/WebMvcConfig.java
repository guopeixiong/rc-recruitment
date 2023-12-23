package com.ruanchuang.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ruanchuang.constant.Constants;
import com.ruanchuang.interceptor.RepeatSubmitInterceptor;
import com.ruanchuang.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2023/7/29
 * @Email peixiongguo@163.com
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Value("${api.need-auth}")
    private String authApi;

    @Value("${file.store-address}")
    private String filePath;

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        registry.addInterceptor(new UserTokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**/auth/**", r -> StpUtil.checkLogin());
            SaRouter.match("/**/admin/**", r -> StpUtil.checkRole(Constants.USER_TYPE_ADMIN));
            SaRouter.match(authApi.split(","))
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    /**
     * 跨域支持
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("file:" + filePath + "/");
    }
}
