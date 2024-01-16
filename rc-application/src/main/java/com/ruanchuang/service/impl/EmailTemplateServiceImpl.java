package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.EmailTemplate;
import com.ruanchuang.domain.dto.AddEmailTemplateDTO;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.EmailTemplateMapper;
import com.ruanchuang.service.EmailTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 邮件模板表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class EmailTemplateServiceImpl extends ServiceImpl<EmailTemplateMapper, EmailTemplate> implements EmailTemplateService {

    /**
     * 更新邮件模板
     * @param emailTemplate
     */
    @Override
    public void updateTemplate(EmailTemplate emailTemplate) {
        if (Objects.isNull(emailTemplate.getId())) {
            throw new ServiceException("id为空");
        }
        EmailTemplate updateInfo = new EmailTemplate()
                .setSubject(StringUtils.isBlank(emailTemplate.getSubject()) ? null : emailTemplate.getSubject())
                .setContent(StringUtils.isBlank(emailTemplate.getContent()) ? null : emailTemplate.getContent())
                .setId(emailTemplate.getId());
        boolean success = this.updateById(updateInfo);
        if (!success) {
            throw new RuntimeException("系统异常, 更新邮件模板失败");
        }
    }

    /**
     * 删除邮件模板
     * @param idsDto
     */
    @Override
    public void deleteTemplate(IdsDto idsDto) {
        boolean success = this.removeBatchByIds(idsDto.getIds());
        if (!success) {
            throw new RuntimeException("系统异常, 删除邮件模板失败");
        }
    }

    /**
     * 获取邮件模板列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<EmailTemplate> getList(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .orderByDesc(EmailTemplate::getCreateTime)
                .select(EmailTemplate::getId,
                        EmailTemplate::getContent,
                        EmailTemplate::getSubject,
                        EmailTemplate::getCreateBy,
                        EmailTemplate::getCreateTime,
                        EmailTemplate::getUpdateBy,
                        EmailTemplate::getUpdateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

    /**
     * 添加邮件模板
     * @param addEmailTemplateDTO
     */
    @Override
    public void addTemplate(AddEmailTemplateDTO addEmailTemplateDTO) {
        boolean success = this.save(new EmailTemplate()
                .setSubject(addEmailTemplateDTO.getTitle())
                .setContent(addEmailTemplateDTO.getContent()));
        if (!success) {
            throw new RuntimeException("系统异常, 添加失败");
        }
    }
}
