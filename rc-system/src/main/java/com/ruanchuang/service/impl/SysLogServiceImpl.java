package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SysLog;
import com.ruanchuang.domain.dto.LogQueryDto;
import com.ruanchuang.mapper.SysLogMapper;
import com.ruanchuang.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统操作日志表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    /**
     * 获取日志标题列表
     *
     * @return
     */
    @Override
    public List<String> titleList(Integer type) {
        return this.lambdaQuery()
                .eq(SysLog::getType, type)
                .select(SysLog::getTitle)
                .groupBy(SysLog::getTitle)
                .list()
                .stream()
                .map(SysLog::getTitle)
                .collect(Collectors.toList());
    }

    /**
     * 获取日志列表
     *
     * @param queryDto
     * @return
     */
    @Override
    public IPage<SysLog> logList(LogQueryDto queryDto) {
        return this.lambdaQuery()
                .eq(SysLog::getType, queryDto.getType())
                .eq(StringUtils.isNotBlank(queryDto.getTitle()), SysLog::getTitle, queryDto.getTitle())
                .eq(StringUtils.isNotBlank(queryDto.getRequestMethod()), SysLog::getRequestMethod, queryDto.getRequestMethod())
                .eq(queryDto.getStatus() != null, SysLog::getStatus, queryDto.getStatus())
                .ge(queryDto.getStartTime() != null, SysLog::getCreateTime, queryDto.getStartTime())
                .le(queryDto.getEndTime() != null, SysLog::getCreateTime, queryDto.getEndTime())
                .orderByDesc(SysLog::getCreateTime)
                .page(new Page<>(queryDto.getPageNum(), queryDto.getPageSize()));
    }
}
