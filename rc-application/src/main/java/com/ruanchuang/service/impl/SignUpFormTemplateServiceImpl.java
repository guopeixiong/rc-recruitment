package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.*;
import com.ruanchuang.domain.dto.SubmitFormDto;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.SignUpFormTemplateMapper;
import com.ruanchuang.service.SignUpFormQuestionService;
import com.ruanchuang.service.SignUpFormTemplateService;
import com.ruanchuang.service.SignUpFromAnswerService;
import com.ruanchuang.service.SignUpRecordInfoService;
import com.ruanchuang.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private SignUpRecordInfoService signUpRecordInfoService;

    @Autowired
    private SignUpFromAnswerService signUpFromAnswerService;

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

    /**
     * 提交报名表单
     * @param formDto
     */
    @Override
    public void submitForm(List<SubmitFormDto> formDto) {
        Long templateId = formDto.get(0).getTemplateId();
        boolean exists = this.baseMapper.exists(Wrappers.<SignUpFormTemplate>lambdaQuery().eq(SignUpFormTemplate::getId, templateId));
        if (!exists) {
            throw new ServiceException("表单不存在");
        }
        SysUser user = LoginUtils.getLoginUser();
        boolean alreadySignUp = signUpRecordInfoService.getBaseMapper().exists(Wrappers.<SignUpRecordInfo>lambdaQuery().eq(SignUpRecordInfo::getTemplateId, templateId).eq(SignUpRecordInfo::getUserId, user.getId()));
        if (alreadySignUp) {
            throw new ServiceException("您已经填写过此报名表, 可前往个人中心-报名记录修改");
        }
        // 必答问题id
        Set<Long> requireQuestionIds = signUpFormQuestionService.lambdaQuery()
                .eq(SignUpFormQuestion::getTemplateId, templateId)
                .eq(SignUpFormQuestion::getIsRequire, Constants.QUESTION_TYPE_REQUIRE)
                .select(SignUpFormQuestion::getId)
                .list().stream()
                .map(SignUpFormQuestion::getId)
                .collect(Collectors.toSet());
        formDto.stream().filter(o ->
                requireQuestionIds.contains(o.getId()) && StringUtils.isBlank(o.getAnswer())
        ).findFirst().ifPresent(question -> {
            throw new ServiceException("请回答所有必答问题");
        });
        // 过滤选择题选项是否有非法提交内容
        formDto.stream().filter(o -> o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE) || o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE))
                .map(SubmitFormDto::getAnswer)
                .filter(answer -> answer.matches("\\d+(,\\d+)*"))
                .findFirst()
                .ifPresent(answer -> {
                    throw new ServiceException("非法提交内容");
                });
        // 保存用户填写记录
        SignUpRecordInfo signUpRecordInfo = new SignUpRecordInfo()
                .setUserId(user.getId())
                .setUserName(user.getFullName())
                .setTemplateId(templateId);
        // 保存答案
        List<SignUpFromAnswer> answers = new ArrayList<>();
        formDto.stream().forEach(o -> {
            SignUpFromAnswer answer = new SignUpFromAnswer()
                    .setUserId(user.getId())
                    .setTemplateId(templateId)
                    .setQuestionId(o.getId());
            if (o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_TEXT)) {
                answer.setType(Constants.QUESTION_ANSWER_TYPE_TEXT)
                        .setTextAnswer(o.getAnswer());
            } else {
                answer.setType(Constants.QUESTION_ANSWER_TYPE_OPTION)
                        .setOptionsAnswer(o.getAnswer());
            }
            answers.add(answer);
        });
        this.saveFormAnswer(signUpRecordInfo, answers);
    }

    /**
     * 保存用户提交报名表答案
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void saveFormAnswer(SignUpRecordInfo signUpRecordInfo, List<SignUpFromAnswer> answers) {
        if (!signUpRecordInfoService.save(signUpRecordInfo)) {
            throw new ServiceException("系统异常, 提交失败");
        }
        if (!signUpFromAnswerService.saveBatch(answers)) {
            throw new ServiceException("系统异常, 提交失败");
        }
    }

}
