package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.SignUpFormQuestion;
import com.ruanchuang.domain.SignUpFormTemplate;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.SignUpFormTemplateMapper;
import com.ruanchuang.service.SignUpFormQuestionService;
import com.ruanchuang.service.SignUpFormTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 报名表模板表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Slf4j
@Service
public class SignUpFormTemplateServiceImpl extends ServiceImpl<SignUpFormTemplateMapper, SignUpFormTemplate> implements SignUpFormTemplateService {

    @Autowired
    private SignUpFormQuestionService signUpFormQuestionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取报名表单
     *
     * @return
     */
    @Override
    public List<SignUpFormQuestion> getForm() {
        List<SignUpFormQuestion> signUpForm = (List<SignUpFormQuestion>) redisTemplate.opsForValue().get(CacheConstants.SIGN_UP_FORM_CACHE_KEY);
        if (signUpForm != null) {
            return signUpForm;
        }
        synchronized (this) {
            signUpForm = (List<SignUpFormQuestion>) redisTemplate.opsForValue().get(CacheConstants.SIGN_UP_FORM_CACHE_KEY);
            if (signUpForm != null) {
                return signUpForm;
            }
            SignUpFormTemplate template = this.baseMapper.selectOne(
                    Wrappers.<SignUpFormTemplate>lambdaQuery()
                            .eq(SignUpFormTemplate::getIsEnabled, Constants.SIGN_UP_FORM_TEMPLATE_STATUS_ENABLE)
                            .select(SignUpFormTemplate::getId)
            );
            if (template == null) {
                log.error("系统中没有启动的模板");
                throw new ServiceException("系统异常");
            }
            signUpForm = signUpFormQuestionService.selectQuestionsByTemplateId(template.getId());
            redisTemplate.opsForValue().set(CacheConstants.SIGN_UP_FORM_CACHE_KEY, signUpForm);
            // 只缓存五分钟, 五分钟后从数据库获取新数据
            redisTemplate.expire(CacheConstants.SIGN_UP_FORM_CACHE_KEY, 5, TimeUnit.MINUTES);
        }
        return signUpForm;
    }
}
