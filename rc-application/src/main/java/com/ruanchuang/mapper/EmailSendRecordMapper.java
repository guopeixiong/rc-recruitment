package com.ruanchuang.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.EmailSendRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruanchuang.domain.dto.EmailSendRecordQueryDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 邮件发送记录表 Mapper 接口
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface EmailSendRecordMapper extends BaseMapper<EmailSendRecord> {

    /**
     * 查询邮件发送记录列表
     *
     * @param emailSendRecordQueryDto
     * @param page
     * @return
     */
    IPage<EmailSendRecord> queryList(@Param("param") EmailSendRecordQueryDto emailSendRecordQueryDto, @Param("page") Page page);
}
