package com.ruanchuang.annotation;

import com.ruanchuang.enums.BusinessType;
import java.lang.annotation.*;

/**
 * 日志记录注解
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 自定义日志内容
     * @return
     */
    String title() default "";

    /**
     * 业务类型
     * @return
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求参数
     * @return
     */
    boolean saveRequestParam() default false;

    /**
     * 是否保存响应结果
     * @return
     */
    boolean saveResponseResult() default false;

    /**
     * 日志类型 0.用户日志 1.后台日志 2.登录日志
     * @return
     */
    int type() default 0;
}
