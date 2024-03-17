package com.ruanchuang.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.*;
import com.ruanchuang.domain.dto.*;
import com.ruanchuang.domain.vo.SignUpFormVo;
import com.ruanchuang.domain.vo.TemplateQuestionVo;
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
    public List<SignUpFormQuestion> getForm(Long id) {
        String redisKey = CacheConstants.SIGN_UP_FORM_CACHE_KEY + (id == null ? "" : id.toString());
        String offRedisKey = CacheConstants.SIGN_UP_OFF+ (id == null ? "" : id.toString());
        if (redisTemplate.hasKey(offRedisKey)) {
            throw new ServiceException("暂未开放报名");
        }
        synchronized (this) {
            if (redisTemplate.hasKey(redisKey)) {
                return redisTemplate.opsForList().range(redisKey, 0, -1);
            }
            SignUpFormTemplate template = this.baseMapper.selectOne(
                    Wrappers.<SignUpFormTemplate>lambdaQuery()
                            .eq(id != null, SignUpFormTemplate::getId, id)
                            .eq(SignUpFormTemplate::getIsEnabled, Constants.SIGN_UP_FORM_TEMPLATE_STATUS_ENABLE)
                            .eq(id == null, SignUpFormTemplate::getType, Constants.SIGN_UP_FROM_TYPE_SIGN_UP)
                            .select(SignUpFormTemplate::getId)
                            .orderByDesc(SignUpFormTemplate::getCreateTime)
                            .last("limit 1")
            );
            if (template == null) {
                log.error("系统中没有启动的模板");
                redisTemplate.opsForValue().set(offRedisKey, Boolean.TRUE);
                redisTemplate.expire(offRedisKey, 5, TimeUnit.MINUTES);
                throw new ServiceException("暂未开放报名");
            }
            List<SignUpFormQuestion> signUpForm = signUpFormQuestionService.selectQuestionsByTemplateId(template.getId());
            redisTemplate.opsForList().rightPushAll(redisKey, signUpForm);
            // 只缓存五分钟, 五分钟后从数据库获取新数据
            redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
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
        SignUpFormTemplate template = this.baseMapper.selectOne(Wrappers.<SignUpFormTemplate>lambdaQuery().eq(SignUpFormTemplate::getId, templateId).select(SignUpFormTemplate::getId, SignUpFormTemplate::getProcessId, SignUpFormTemplate::getName));
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
                .setTemplateName(template.getName())
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
        SignUpRecordInfo recordInfo = signUpRecordInfoService.getBaseMapper().selectOne(
                Wrappers.<SignUpRecordInfo>lambdaQuery()
                        .eq(SignUpRecordInfo::getUserId, userId)
                        .eq(SignUpRecordInfo::getTemplateId, question.getTemplateId()));
        if (Objects.isNull(recordInfo)) {
            throw new ServiceException("您尚未报名");
        }
        if (!signUpProcessService.getDefaultProcessStatusId(recordInfo.getProcessId()).equals(recordInfo.getCurrentProcessStatusId())) {
            throw new ServiceException("已经入面试流程，无法修改报名表");
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
     * 获取所有报名表
     * @return
     */
    @Override
    public List<SignUpFormTemplate> getList() {
        return this.lambdaQuery()
                .orderByDesc(SignUpFormTemplate::getCreateTime)
                .select(SignUpFormTemplate::getId,
                        SignUpFormTemplate::getName,
                        SignUpFormTemplate::getProcessId)
                .list();
    }

    /**
     * 编辑报名表
     * @param editTemplateDto
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void UpdateTemplate(EditTemplateDto editTemplateDto) {
        Long count = signUpRecordInfoService.lambdaQuery()
                .eq(SignUpRecordInfo::getTemplateId, editTemplateDto.getId())
                .count();
        if (count > 0) {
            throw new ServiceException("该报名表已有报名记录，无法编辑");
        }
        List<SignUpFormQuestion> questions = new ArrayList<>(editTemplateDto.getQuestions().size());
        List<TemplateQuestionOptions> options = new ArrayList<>();
        Snowflake snowflake = IdUtil.getSnowflake();
        for (int i = 0; i < editTemplateDto.getQuestions().size(); i++) {
            TemplateQuestionVo questionVo = editTemplateDto.getQuestions().get(i);
            SignUpFormQuestion question = new SignUpFormQuestion()
                    .setId(snowflake.nextId())
                    .setTemplateId(editTemplateDto.getId())
                    .setSort(i)
                    .setContent(questionVo.getContent())
                    .setType(questionVo.getType())
                    .setIsRequire(Integer.parseInt(questionVo.getIsRequire()));
            if (!question.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_TEXT)) {
                for (int j = 0; j < questionVo.getOptions().size(); j++) {
                    TemplateQuestionOptions option = new TemplateQuestionOptions()
                            .setTemplateId(editTemplateDto.getId())
                            .setQuestionId(question.getId())
                            .setContent(questionVo.getOptions().get(j));
                    options.add(option);
                }
            }
            questions.add(question);
        }
        SignUpFormTemplate template = new SignUpFormTemplate()
                .setId(editTemplateDto.getId())
                .setProcessId(editTemplateDto.getProcessId())
                .setName(editTemplateDto.getName())
                .setIsEnabled(editTemplateDto.getIsEnabled());
        boolean success = this.updateById(template);
        if (!success) {
            throw new ServiceException("系统异常, 更新失败");
        }
        if (editTemplateDto.getQuestions().isEmpty()) {
            return;
        }
        signUpFormQuestionService.lambdaUpdate()
                .eq(SignUpFormQuestion::getTemplateId, editTemplateDto.getId())
                .remove();
        templateQuestionOptionsService.lambdaUpdate()
                .eq(TemplateQuestionOptions::getTemplateId, editTemplateDto.getId())
                .remove();
        success = signUpFormQuestionService.saveBatch(questions);
        if (!success) {
            throw new ServiceException("系统异常, 保存失败");
        }
        if (options.isEmpty()) {
            return;
        }
        success = templateQuestionOptionsService.saveBatch(options);
        if (!success) {
            throw new ServiceException("系统异常, 保存失败");
        }
        redisTemplate.delete(CacheConstants.SIGN_UP_OFF);
        redisTemplate.delete(CacheConstants.SIGN_UP_FORM_CACHE_KEY);
    }

    /**
     * 后台查询报名表详情
     * @param id
     * @return
     */
    @Override
    public List<TemplateQuestionVo> getDetail(Long id) {
        List<SignUpFormQuestion> qusList = signUpFormQuestionService.lambdaQuery()
                .eq(SignUpFormQuestion::getTemplateId, id)
                .select(SignUpFormQuestion::getContent,
                        SignUpFormQuestion::getIsRequire,
                        SignUpFormQuestion::getType,
                        SignUpFormQuestion::getId)
                .orderByAsc(SignUpFormQuestion::getSort)
                .list();
        if (qusList.isEmpty()) {
            return List.of();
        }
        List<TemplateQuestionVo> result = new ArrayList<>(qusList.size());
        qusList.stream().forEach(q ->
            result.add(
                    new TemplateQuestionVo()
                            .setId(q.getId())
                            .setType(q.getType())
                            .setIsRequire(q.getIsRequire().toString())
                            .setContent(q.getContent()))
        );
        List<TemplateQuestionOptions> optionslist = templateQuestionOptionsService.lambdaQuery()
                .eq(TemplateQuestionOptions::getTemplateId, id)
                .select(TemplateQuestionOptions::getContent,
                        TemplateQuestionOptions::getQuestionId)
                .list();
        if (optionslist.isEmpty()) {
            return result;
        }
        result.stream().filter(q -> !q.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_TEXT))
                .forEach(q ->
                    q.setOptions(
                            optionslist.stream().filter(opt -> opt.getQuestionId().equals(q.getId())).map(TemplateQuestionOptions::getContent).collect(Collectors.toList()))
                );
        return result;
    }

    /**
     * 获取流程列表
     * @return
     */
    @Override
    public List<SignUpProcess> getProcessList() {
        return signUpProcessService.lambdaQuery()
                .eq(SignUpProcess::getEnable, Constants.SIGN_UP_PROCESS_STATUS_ENABLE)
                .orderByDesc(SignUpProcess::getCreateTime)
                .select(SignUpProcess::getId,
                        SignUpProcess::getName)
                .list();
    }

    /**
     * 添加报名表
     * @param addTemplateDto
     */
    @Override
    public void add(AddTemplateDto addTemplateDto) {
        boolean exists = signUpProcessService.lambdaQuery()
                .eq(SignUpProcess::getId, addTemplateDto.getProcessId())
                .exists();
        if (!exists) {
            throw new ServiceException("流程不存在");
        }
        SignUpFormTemplate template = new SignUpFormTemplate()
                .setName(addTemplateDto.getName())
                .setType(addTemplateDto.getType())
                .setProcessId(addTemplateDto.getProcessId());
        boolean success = this.save(template);
        if (!success) {
            throw new ServiceException("系统异常, 创建失败");
        }
    }

    /**
     * 批量删除报名表
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        boolean success = this.removeBatchByIds(ids);
        if (!success) {
            throw new ServiceException("系统异常, 删除失败");
        }
    }

    /**
     * 后台查询报名表列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<SignUpFormVo> getFormList(BaseQueryDto baseQueryDto) {
        Page page = new Page(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
        return baseMapper.selectFormList(page);
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
