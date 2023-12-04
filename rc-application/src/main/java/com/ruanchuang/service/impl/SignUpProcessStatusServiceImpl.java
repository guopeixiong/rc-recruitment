package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SignUpProcessStatus;
import com.ruanchuang.mapper.SignUpProcessStatusMapper;
import com.ruanchuang.service.SignUpProcessStatusService;
import org.springframework.stereotype.Service;

/**
 * @author guopx
 * @since 2023/12/4
 */
@Service
public class SignUpProcessStatusServiceImpl extends ServiceImpl<SignUpProcessStatusMapper, SignUpProcessStatus> implements SignUpProcessStatusService {
}
