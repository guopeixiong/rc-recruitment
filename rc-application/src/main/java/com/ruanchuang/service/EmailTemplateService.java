package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruanchuang.domain.EmailTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.dto.AddEmailTemplateDTO;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;

/**
 * <p>
 * 邮件模板表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface EmailTemplateService extends IService<EmailTemplate> {

    /**
     * 添加邮件模板
     * @param addEmailTemplateDTO
     */
    void addTemplate(AddEmailTemplateDTO addEmailTemplateDTO);

    /**
     * 获取邮件模板列表
     * @param baseQueryDto
     * @return
     */
    IPage<EmailTemplate> getList(BaseQueryDto baseQueryDto);

    /**
     * 删除邮件模板
     * @param idsDto
     */
    void deleteTemplate(IdsDto idsDto);

    /**
     * 更新邮件模板
     * @param emailTemplate
     */
    void updateTemplate(EmailTemplate emailTemplate);
}
