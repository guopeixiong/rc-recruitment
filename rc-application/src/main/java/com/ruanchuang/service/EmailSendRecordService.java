package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruanchuang.domain.EmailSendRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.dto.EmailSendRecordQueryDto;

/**
 * <p>
 * 邮件发送记录表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface EmailSendRecordService extends IService<EmailSendRecord> {

    /**
     * 查询邮件发送记录列表
     *
     * @param emailSendRecordQueryDto
     * @return
     */
    IPage<EmailSendRecord> queryList(EmailSendRecordQueryDto emailSendRecordQueryDto);
}
