package com.ruanchuang.service.impl;

import com.ruanchuang.domain.SignUpFromAnswer;
import com.ruanchuang.mapper.SignUpFromAnswerMapper;
import com.ruanchuang.service.SignUpFromAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报名表回答表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class SignUpFromAnswerServiceImpl extends ServiceImpl<SignUpFromAnswerMapper, SignUpFromAnswer> implements SignUpFromAnswerService {

    /**
     * 查询用户报名表问题修改次数
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    public Integer getNumOfQuestionUpdateTimes(Long userId, Long questionId) {
        return this.baseMapper.getTheNumOfQuestionUpdateTimes(userId, questionId);
    }
}
