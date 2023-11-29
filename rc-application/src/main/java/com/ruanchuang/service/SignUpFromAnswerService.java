package com.ruanchuang.service;

import com.ruanchuang.domain.SignUpFromAnswer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报名表回答表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpFromAnswerService extends IService<SignUpFromAnswer> {

    /**
     * 查询用户报名表问题修改次数
     * @param userId
     * @param questionId
     * @return
     */
    Integer getNumOfQuestionUpdateTimes(Long userId, Long questionId);

}
