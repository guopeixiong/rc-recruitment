package com.ruanchuang.service.impl;

import com.ruanchuang.domain.EmailSendRecord;
import com.ruanchuang.mapper.EmailSendRecordMapper;
import com.ruanchuang.service.EmailSendRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
