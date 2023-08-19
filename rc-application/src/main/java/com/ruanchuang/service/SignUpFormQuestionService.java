package com.ruanchuang.service;

import com.ruanchuang.domain.SignUpFormQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报名表问题表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpFormQuestionService extends IService<SignUpFormQuestion> {

    /**
     * 根据模板id查询问题列表
     * @param id
     * @return
     */
    List<SignUpFormQuestion> selectQuestionsByTemplateId(Long id);
}
