package com.ruanchuang.mapper;

import com.ruanchuang.domain.SignUpFromAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报名表回答表 Mapper 接口
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpFromAnswerMapper extends BaseMapper<SignUpFromAnswer> {

    /**
     * 查询用户报名表问题修改次数
     * @param questionId
     * @param userId
     * @return
     */
    Integer getTheNumOfQuestionUpdateTimes(@Param("userId") Long userId, @Param("questionId") Long questionId);

}
