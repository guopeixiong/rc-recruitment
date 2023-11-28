package com.ruanchuang.service;

import com.ruanchuang.domain.SignUpFormQuestion;
import com.ruanchuang.domain.SignUpFormTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.dto.SubmitFormDto;
import com.ruanchuang.domain.dto.UpdateSignUpFormDto;

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
     * 修改报名表
     * @param updateSignUpFormDto
     */
    void updateForm(UpdateSignUpFormDto updateSignUpFormDto);
}
