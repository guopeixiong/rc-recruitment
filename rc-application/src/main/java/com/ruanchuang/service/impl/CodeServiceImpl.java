package com.ruanchuang.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.service.CodeService;
import com.ruanchuang.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailUtils emailUtils;

    /**
     * 发送验证码
     * @param email
     * @param codeType
     * @return
     */
    @Override
    public String sendCode(String email, String codeType) {
        String code = RandomUtil.randomInt(100000, 999999) + "";
        if (codeType.equals(CacheConstants.CAPTCHA_CODE_KEY_LOGIN)) {
            redisTemplate.opsForValue().set(CacheConstants.CAPTCHA_CODE_KEY_LOGIN + email, code);
            redisTemplate.expire(CacheConstants.CAPTCHA_CODE_KEY_LOGIN + email, 5, TimeUnit.MINUTES);
            codeType = "登录验证码";
        } else if (codeType.equals(CacheConstants.CAPTCHA_CODE_KEY_REGISTER)) {
            redisTemplate.opsForValue().set(CacheConstants.CAPTCHA_CODE_KEY_REGISTER + email, code);
            redisTemplate.expire(CacheConstants.CAPTCHA_CODE_KEY_REGISTER + email, 5, TimeUnit.MINUTES);
            codeType = "注册验证码";
        }
        emailUtils.sendCode(email, code, codeType);
        return null;
    }
}
