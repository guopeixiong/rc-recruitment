package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.EmailSendRecord;
import com.ruanchuang.domain.dto.EmailSendRecordQueryDto;
import com.ruanchuang.mapper.EmailSendRecordMapper;
import com.ruanchuang.service.EmailSendRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮件发送记录表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class EmailSendRecordServiceImpl extends ServiceImpl<EmailSendRecordMapper, EmailSendRecord> implements EmailSendRecordService {

    /**
     * 查询邮件发送记录列表
     *
     * @param emailSendRecordQueryDto
     * @return
     */
    @Override
    public IPage<EmailSendRecord> queryList(EmailSendRecordQueryDto emailSendRecordQueryDto) {
        return this.baseMapper.queryList(emailSendRecordQueryDto, new Page<>(emailSendRecordQueryDto.getPageNum(), emailSendRecordQueryDto.getPageSize()));
    }
}
