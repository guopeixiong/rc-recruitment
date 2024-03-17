package com.ruanchuang.constant;

/**
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
public class CacheConstants {

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_USER_KEY = "login_user:";

    /**
     * 登录验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY_LOGIN = "captcha_codes_login:";

    /**
     * 登录验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY_REGISTER = "captcha_codes_register:";

    /**
     * 忘记密码验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY_FORGET_PWD = "captcha_codes_forget_pwd:";

    /**
     * 修改密码验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY_UPDATE_PWD = "captcha_codes_update_pwd:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 禁止登录 redis key
     */
    public static final String USER_FORBIDDEN = "user_forbidden:";

    /**
     * 报名表缓存 redis key
     */
    public static final String SIGN_UP_FORM_CACHE_KEY = "sign_up_form_cache";

    /**
     * 报名通道关闭
     */
    public static final String SIGN_UP_OFF = "sign_up_off";

    /**
     * 常见问题缓存 redis key
     */
    public static final String COMMON_QA_INFO_CACHE_KEY = "common_qa_info_cache";

    /**
     * 报名流程缓存 redis key
     */
    public static final String PROCESS_CACHE_KEY = "process_cache";

    /**
     * 首页介绍缓存 redis key
     */
    public static final String INDEX_INTRO_CACHE_KEY = "index_intro_cache";

    /**
     * 首页轮播图缓存 redis key
     */
    public static final String INDEX_IMAGE_CACHE_KEY = "index_image_cache";

    /**
     * 活动信息缓存 redis key
     */
    public static final String ACTIVITY_INFO_CACHE_KEY = "activity_info_cache";

}
