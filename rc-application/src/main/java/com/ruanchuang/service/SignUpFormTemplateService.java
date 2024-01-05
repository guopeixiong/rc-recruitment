package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SignUpFormQuestion;
import com.ruanchuang.domain.SignUpFormTemplate;
import com.ruanchuang.domain.TemplateQuestionOptions;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.SubmitFormDto;
import com.ruanchuang.domain.dto.UpdateSignUpFormDto;
import com.ruanchuang.domain.vo.SignUpFormVo;

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
    List<SignUpFormQuestion> getForm();

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
}
