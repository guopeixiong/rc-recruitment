package com.ruanchuang.constant;

/**
 * 通用常量
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
public class Constants {

    /**
     * 用户类型-普通用户
     */
    public static final String USER_TYPE_NORMAL = "normal";

    /**
     * 用户类型-管理员
     */
    public static final String USER_TYPE_ADMIN = "admin";

    /**
     * 用户状态-禁用
     */
    public static final Integer USER_STATUS_DISABLE = 1;

    /**
     * 用户状态-启用
     */
    public static final Integer USER_STATUS_ENABLE = 0;


    /**
     * 日志类型 用户
     */
    public static int LOG_TYPE_USER = 0;

    /**
     * 日志类型 后台
     */
    public static int LOG_TYPE_ADMIN = 1;

    /**
     * 日志类型 登录
     */
    public static int LOG_TYPE_LOGIN = 2;

    /**
     * 日志状态 异常
     */
    public static int LOG_STATUS_ERROR = 1;

}
