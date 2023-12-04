package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SignUpProcess;
import com.ruanchuang.mapper.SignUpProcessMapper;
import com.ruanchuang.service.SignUpProcessService;
import org.springframework.stereotype.Service;

/**
 * @author guopx
 * @since 2023/12/4
 */
@Service
public class SignUpProcessServiceImpl extends ServiceImpl<SignUpProcessMapper, SignUpProcess> implements SignUpProcessService {
}
