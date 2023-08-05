package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SysLog;
import com.ruanchuang.mapper.SysLogMapper;
import com.ruanchuang.service.SysLogService;
import org.springframework.stereotype.Service;

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

}
