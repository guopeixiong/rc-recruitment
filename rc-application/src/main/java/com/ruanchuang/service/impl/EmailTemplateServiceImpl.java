package com.ruanchuang.service.impl;

import com.ruanchuang.domain.EmailTemplate;
import com.ruanchuang.mapper.EmailTemplateMapper;
import com.ruanchuang.service.EmailTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
