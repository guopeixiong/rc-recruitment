package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SysLog;
import com.ruanchuang.domain.dto.LogQueryDto;

import java.util.List;

/**
 * <p>
 * 系统操作日志表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 获取日志列表
     *
     * @param queryDto
     * @return
     */
    IPage<SysLog> logList(LogQueryDto queryDto);

    /**
     * 获取日志标题列表
     *
     * @return
     */
    List<String> titleList(Integer type);

    /**
     * 获取最新100条错误日志
     *
     * @return
     */
    List<SysLog> lastErrorLog();
}
