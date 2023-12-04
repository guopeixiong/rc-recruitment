package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.*;
import com.ruanchuang.domain.dto.SubmitFormDto;
import com.ruanchuang.domain.dto.UpdateSignUpFormDto;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.SignUpFormTemplateMapper;
import com.ruanchuang.service.*;
import com.ruanchuang.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Value("${user.max-form-update-times}")
    private Integer maxFormUpdateTimes;

    @Autowired
    private TemplateQuestionOptionsService templateQuestionOptionsService;

    @Autowired
    private SignUpProcessService signUpProcessService;

    /**
     * 获取报名表单
     *
     * @return
     */
    @Override
    public List<SignUpFormQuestion> getForm() {
        if (redisTemplate.hasKey(CacheConstants.SIGN_UP_OFF)) {
            throw new ServiceException("暂未开放报名");
        }
        synchronized (this) {
            if (redisTemplate.hasKey(CacheConstants.SIGN_UP_FORM_CACHE_KEY)) {
                return redisTemplate.opsForList().range(CacheConstants.SIGN_UP_FORM_CACHE_KEY, 0, -1);
            }
            SignUpFormTemplate template = this.baseMapper.selectOne(
                    Wrappers.<SignUpFormTemplate>lambdaQuery()
                            .eq(SignUpFormTemplate::getIsEnabled, Constants.SIGN_UP_FORM_TEMPLATE_STATUS_ENABLE)
                            .select(SignUpFormTemplate::getId)
            );
            if (template == null) {
                log.error("系统中没有启动的模板");
                redisTemplate.opsForValue().set(CacheConstants.SIGN_UP_OFF, Boolean.TRUE);
                redisTemplate.expire(CacheConstants.SIGN_UP_OFF, 5, TimeUnit.MINUTES);
                throw new ServiceException("暂未开放报名");
            }
            List<SignUpFormQuestion> signUpForm = signUpFormQuestionService.selectQuestionsByTemplateId(template.getId());
            redisTemplate.opsForList().rightPushAll(CacheConstants.SIGN_UP_FORM_CACHE_KEY, signUpForm);
            // 只缓存五分钟, 五分钟后从数据库获取新数据
            redisTemplate.expire(CacheConstants.SIGN_UP_FORM_CACHE_KEY, 5, TimeUnit.MINUTES);
            return signUpForm;
        }
    }

    /**
     * 提交报名表单
     *
     * @param formDto
     */
    @Override
    public void submitForm(List<SubmitFormDto> formDto) {
        Long templateId = formDto.get(0).getTemplateId();
        SignUpFormTemplate template = this.baseMapper.selectOne(Wrappers.<SignUpFormTemplate>lambdaQuery().eq(SignUpFormTemplate::getId, templateId).select(SignUpFormTemplate::getId, SignUpFormTemplate::getProcessId));
        if (Objects.isNull(template)) {
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
        formDto.stream()
                .filter(o -> o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE) || o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE))
                .filter(o -> StringUtils.isNotBlank(o.getAnswer()))
                .map(SubmitFormDto::getAnswer)
                .filter(answer -> !answer.matches("\\d+(,\\d+)*"))
                .findFirst()
                .ifPresent(answer -> {
                    throw new ServiceException("非法提交内容");
                });
        // 保存用户填写记录
        SignUpRecordInfo signUpRecordInfo = new SignUpRecordInfo()
                .setUserId(user.getId())
                .setUserName(user.getFullName())
                .setTemplateId(templateId)
                .setProcessId(template.getProcessId())
                .setCurrentProcessStatusId(signUpProcessService.getDefaultProcessStatusId(template.getProcessId()));
        // 保存答案
        List<SignUpFromAnswer> answers = new ArrayList<>();
        formDto.stream().forEach(o -> {
            // 过滤没有作答的非必答题
            if (StringUtils.isBlank(o.getAnswer())) {
                return;
            }
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
     * 查询选择题选项
     *
     * @param id
     * @return
     */
    @Override
    public List<TemplateQuestionOptions> queryChoiceQuestion(Long id) {
        return templateQuestionOptionsService.lambdaQuery()
                .eq(TemplateQuestionOptions::getQuestionId, id)
                .select(TemplateQuestionOptions::getId,
                        TemplateQuestionOptions::getContent)
                .list();
    }

    /**
     * 修改报名表
     *
     * @param updateSignUpFormDto
     */
    @Override
    public void updateForm(UpdateSignUpFormDto updateSignUpFormDto) {
        Long userId = LoginUtils.getLoginUser().getId();
        SignUpFormQuestion question = signUpFormQuestionService.getBaseMapper().selectOne(
                Wrappers.<SignUpFormQuestion>lambdaQuery()
                        .eq(SignUpFormQuestion::getId, updateSignUpFormDto.getId())
                        .select(SignUpFormQuestion::getTemplateId,
                                SignUpFormQuestion::getType)
        );
        if (question == null) {
            throw new ServiceException("问题不存在");
        }
        boolean exists = signUpRecordInfoService.lambdaQuery()
                .eq(SignUpRecordInfo::getUserId, userId)
                .eq(SignUpRecordInfo::getTemplateId, question.getTemplateId())
                .exists();
        if (!exists) {
            throw new ServiceException("您尚未报名");
        }
        queryTheRestOfQuestionUpdateTimes(updateSignUpFormDto.getId());
        // 处理单项选择题和多项选择题
        if (!question.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_TEXT) && !updateSignUpFormDto.getAnswer().matches("\\d+(,\\d+)*")) {
            throw new ServiceException("非法提交内容");
        }
        // 删除原来的答案
        signUpFromAnswerService.lambdaUpdate()
                .eq(SignUpFromAnswer::getUserId, userId)
                .eq(SignUpFromAnswer::getTemplateId, question.getTemplateId())
                .eq(SignUpFromAnswer::getQuestionId, updateSignUpFormDto.getId())
                .remove();
        SignUpFromAnswer signUpFromAnswer = new SignUpFromAnswer()
                .setUserId(userId)
                .setTemplateId(question.getTemplateId())
                .setQuestionId(updateSignUpFormDto.getId())
                .setType(question.getType())
                .setOptionsAnswer(updateSignUpFormDto.getAnswer());
        if (question.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_TEXT)) {
            signUpFromAnswer.setType(Constants.QUESTION_ANSWER_TYPE_TEXT)
                    .setTextAnswer(updateSignUpFormDto.getAnswer());
        } else {
            signUpFromAnswer.setType(Constants.QUESTION_ANSWER_TYPE_OPTION)
                    .setOptionsAnswer(updateSignUpFormDto.getAnswer());
        }
        // 保存新答案
        boolean save = signUpFromAnswerService.save(signUpFromAnswer);
        if (!save) {
            throw new ServiceException("系统异常, 提交失败");
        }
    }

    /**
     * 查询问题剩余修改次数
     *
     * @param id
     * @return
     */
    @Override
    public Integer queryTheRestOfQuestionUpdateTimes(Long id) {
        Integer count = signUpFromAnswerService.getNumOfQuestionUpdateTimes(LoginUtils.getLoginUser().getId(), id);
        if (count >= maxFormUpdateTimes) {
            throw new ServiceException("该问题修改次数已经超过" + maxFormUpdateTimes + "次,无法修改");
        }
        return maxFormUpdateTimes - count;
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
