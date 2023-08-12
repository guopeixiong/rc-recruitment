package com.ruanchuang.service;

/**
 * 验证码服务类
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
public interface CodeService {

    /**
     * 发送验证码
     * @param email
     * @param CodeType
     * @return
     */
    String sendCode(String email, String CodeType);

    /**
     * 发送忘记密码验证码
     * @param email
     */
    void sendForgetPwdCode(String email);

    /**
     * 发送修改密码验证码
     * @param email
     */
    void sendUpdatePwdCode(String email);
}
