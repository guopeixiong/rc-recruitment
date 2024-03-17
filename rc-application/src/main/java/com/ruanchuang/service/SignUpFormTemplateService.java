package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SignUpFormQuestion;
import com.ruanchuang.domain.SignUpFormTemplate;
import com.ruanchuang.domain.SignUpProcess;
import com.ruanchuang.domain.TemplateQuestionOptions;
import com.ruanchuang.domain.dto.*;
import com.ruanchuang.domain.vo.SignUpFormVo;
import com.ruanchuang.domain.vo.TemplateQuestionVo;

import java.util.List;

/**
 * <p>
 * 报名表模板表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpFormTemplateService extends IService<SignUpFormTemplate> {

    /**
     * 获取报名表单
     * @return
     */
    List<SignUpFormQuestion> getForm(Long id);

    /**
     * 提交报名表
     * @param formDto
     */
    void submitForm(List<SubmitFormDto> formDto);

    /**
     * 查询选择题选项
     * @param id
     * @return
     */
    List<TemplateQuestionOptions> queryChoiceQuestion(Long id);
    /**
     * 修改报名表
     * @param updateSignUpFormDto
     */
    void updateForm(UpdateSignUpFormDto updateSignUpFormDto);

    /**
     * 查询问题剩余修改次数
     * @param id
     * @return
     */
    Integer queryTheRestOfQuestionUpdateTimes(Long id);

    /**
     * 后台查询报名表列表
     * @param baseQueryDto
     * @return
     */
    IPage<SignUpFormVo> getFormList(BaseQueryDto baseQueryDto);

    /**
     * 批量删除报名表
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 添加报名表
     * @param addTemplateDto
     */
    void add(AddTemplateDto addTemplateDto);

    /**
     * 获取流程列表
     * @return
     */
    List<SignUpProcess> getProcessList();

    /**
     * 后台查询报名表详情
     * @param id
     * @return
     */
    List<TemplateQuestionVo> getDetail(Long id);

    /**
     * 编辑报名表
     * @param editTemplateDto
     */
    void UpdateTemplate(EditTemplateDto editTemplateDto);

    /**
     * 获取所有报名表
     * @return
     */
    List<SignUpFormTemplate> getList();

}
