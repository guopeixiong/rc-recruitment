package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.*;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SendEmailDto;
import com.ruanchuang.domain.dto.SignUpRecordQueryDto;
import com.ruanchuang.domain.vo.SignUpDetailVo;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.SignUpRecordInfoMapper;
import com.ruanchuang.service.*;
import com.ruanchuang.utils.EmailUtils;
import com.ruanchuang.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 报名记录信息表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class SignUpRecordInfoServiceImpl extends ServiceImpl<SignUpRecordInfoMapper, SignUpRecordInfo> implements SignUpRecordInfoService {

    @Autowired
    private TemplateQuestionOptionsService templateQuestionOptionsService;

    @Autowired
    private SignUpProcessService signUpProcessService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private EmailSendRecordService emailSendRecordService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    /**
     * 用户分页查询报名记录列表
     *
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<SignUpRecordInfo> queryUserSignUpRecord(BaseQueryDto baseQueryDto) {
        Page<SignUpRecordInfo> page = this.lambdaQuery()
                .eq(SignUpRecordInfo::getUserId, LoginUtils.getLoginUser().getId())
                .select(SignUpRecordInfo::getId,
                        SignUpRecordInfo::getCreateTime,
                        SignUpRecordInfo::getTemplateId,
                        SignUpRecordInfo::getProcessId,
                        SignUpRecordInfo::getProcessEnd,
                        SignUpRecordInfo::getCurrentProcessStatusId)
                .orderByDesc(SignUpRecordInfo::getCreateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
        page.getRecords().stream().forEach(record ->
            record.setCurrentProcess(signUpProcessService.getProcessStatusNameById(record.getProcessId(), record.getCurrentProcessStatusId()))
        );
        return page;
    }

    /**
     * 发送邮件
     * @param sendEmailDto
     */
    @Override
    public void sendEmail(SendEmailDto sendEmailDto) {
        List<SysUser> sysUsers = sysUserService.lambdaQuery()
                .in(SysUser::getId, sendEmailDto.getTargetIds())
                .select(SysUser::getId,
                        SysUser::getEmail)
                .list();
        List<EmailSendRecord> emailSendRecords = new ArrayList<>(sysUsers.size());
        if (sendEmailDto.getSaveAsTemplate().equals(Constants.SAVE_EMAIL_TEMPLATE)) {
            emailTemplateService.save(new EmailTemplate()
                    .setSubject(sendEmailDto.getTitle())
                    .setContent(sendEmailDto.getContent()));
        }
        sysUsers.stream().forEach(user -> {
            emailUtils.sendNotification(sendEmailDto.getTitle(), sendEmailDto.getContent(), user.getEmail());
            emailSendRecords.add(new EmailSendRecord()
                    .setSubject(sendEmailDto.getTitle())
                    .setContent(sendEmailDto.getContent())
                    .setTargetEmail(user.getEmail())
                    .setUserId(user.getId()));
        });
        emailSendRecordService.saveBatch(emailSendRecords);
    }

    /**
     * 查询最近报名记录
     * @return
     */
    @Override
    public List<SignUpRecordInfo> getLastSignUp() {
        return this.lambdaQuery()
                .orderByDesc(SignUpRecordInfo::getCreateTime)
                .select(SignUpRecordInfo::getUserName,
                        SignUpRecordInfo::getCreateTime)
                .last("limit 100")
                .list();
    }

    /**
     * 变更流程状态为已经终止
     * @param idsDto
     */
    @Override
    public void endStatus(IdsDto idsDto) {
        List<SignUpRecordInfo> signUpRecords = new ArrayList<>(idsDto.getIds().size());
        idsDto.getIds().stream().forEach(id -> {
            signUpRecords.add(new SignUpRecordInfo().setId(id).setProcessEnd(Constants.SIGN_UP_PROCESS_STATUS_END));
        });
        boolean success = this.updateBatchById(signUpRecords);
        if (!success) {
            throw new ServiceException("系统异常，修改失败，请稍后再试");
        }
    }

    /**
     * 变更流程状态为下一个流程
     * @param idsDto
     */
    @Override
    public void nextStatus(IdsDto idsDto) {
        List<SignUpRecordInfo> signUpRecords = this.lambdaQuery()
                .in(SignUpRecordInfo::getId, idsDto.getIds())
                .select(SignUpRecordInfo::getId,
                        SignUpRecordInfo::getProcessId,
                        SignUpRecordInfo::getCurrentProcessStatusId)
                .list();
        changeStatusToNext(signUpRecords);
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void changeStatusToNext(List<SignUpRecordInfo> signUpRecords) {
        signUpRecords.stream().forEach(item -> {
            Long nextProcessStatusId = signUpProcessService.getNextProcessStatusId(item.getProcessId(), item.getCurrentProcessStatusId());
            item.setCurrentProcessStatusId(nextProcessStatusId)
                    .setProcessId(null);
            boolean success = this.updateById(item);
            if (!success) {
                throw new ServiceException("更新失败，请稍后再试");
            }
        });
    }

    /**
     * 查询报名记录列表
     * @param signUpRecordQueryDto
     * @return
     */
    @Override
    public IPage<SignUpRecordInfo> getList(SignUpRecordQueryDto signUpRecordQueryDto) {
        Page<SignUpRecordInfo> page = this.lambdaQuery()
                .eq(Objects.nonNull(signUpRecordQueryDto.getTemplateId()), SignUpRecordInfo::getTemplateId, signUpRecordQueryDto.getTemplateId())
                .eq(Objects.nonNull(signUpRecordQueryDto.getStatusId()), SignUpRecordInfo::getCurrentProcessStatusId, signUpRecordQueryDto.getStatusId())
                .orderByDesc(SignUpRecordInfo::getCreateTime)
                .select(SignUpRecordInfo::getId,
                        SignUpRecordInfo::getUserName,
                        SignUpRecordInfo::getUserId,
                        SignUpRecordInfo::getProcessId,
                        SignUpRecordInfo::getProcessEnd,
                        SignUpRecordInfo::getUpdateBy,
                        SignUpRecordInfo::getUpdateTime,
                        SignUpRecordInfo::getCurrentProcessStatusId,
                        SignUpRecordInfo::getCreateTime)
                .page(new Page<>(signUpRecordQueryDto.getPageNum(), signUpRecordQueryDto.getPageSize()));
        page.getRecords().stream().forEach(record -> {
            String processStatusName = signUpProcessService.getProcessStatusNameById(record.getProcessId(), record.getCurrentProcessStatusId());
            record.setCurrentProcess(processStatusName);
            record.setProcessId(null);
            record.setCurrentProcessStatusId(null);
        });
        return page;
    }

    /**
     * 查询报名详情
     *
     * @param id
     * @return
     */
    @Override
    public List<SignUpDetailVo> querySignUpDetail(Long id) {
        if (Objects.isNull(id)) {
            throw new ServiceException("非法入参");
        }
        SignUpRecordInfo signUpRecordInfo = this.baseMapper.selectOne(Wrappers.<SignUpRecordInfo>lambdaQuery()
                .eq(SignUpRecordInfo::getId, id)
                .select(SignUpRecordInfo::getTemplateId,
                        SignUpRecordInfo::getUserId));
        if (Objects.isNull(signUpRecordInfo)) {
            throw new ServiceException("报名记录不存在");
        }
        List<SignUpDetailVo> signUpDetailVos = this.baseMapper.querySignUpDetail(id, signUpRecordInfo.getTemplateId(), signUpRecordInfo.getUserId());
        // 将选择题目选项id放入集合
        List<String> optIds = signUpDetailVos.stream().filter(o -> o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE) && Objects.nonNull(o.getOptAnswer()))
                .map(SignUpDetailVo::getOptAnswer)
                .collect(Collectors.toList());
        signUpDetailVos.stream().filter(o -> o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE) && Objects.nonNull(o.getOptAnswer()))
                .map(SignUpDetailVo::getOptAnswer)
                .forEach(ids -> Arrays.stream(ids.split(",")).forEach(optId -> optIds.add(optId)));
        if (optIds.isEmpty()) {
            return signUpDetailVos;
        }
        List<TemplateQuestionOptions> optContents = templateQuestionOptionsService.lambdaQuery()
                .in(TemplateQuestionOptions::getId, optIds)
                .select(TemplateQuestionOptions::getContent,
                        TemplateQuestionOptions::getId,
                        TemplateQuestionOptions::getQuestionId)
                .list();
        signUpDetailVos.stream().filter(o -> Objects.nonNull(o.getOptAnswer()) && (o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE) || o.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE)))
                .forEach(record -> {
                    switch (record.getType().intValue()) {
                        case Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE:
                            // 此处有坑, 因为数据库中存储的选项id是字符串, 所以需要转换成字符串, 也就是下面的o.getId().toString()
                            record.setOptAnswer(optContents.stream().filter(o -> o.getId().toString().equals(record.getOptAnswer())).findFirst().map(TemplateQuestionOptions::getContent).orElse(null));
                            break;
                        case Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE:
                            record.setOptAnswer(optContents.stream().filter(o -> o.getQuestionId().equals(record.getQuestionId())).map(TemplateQuestionOptions::getContent).collect(Collectors.joining(",")));
                    }
                });
        return signUpDetailVos;
    }

}
