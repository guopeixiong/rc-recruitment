package com.ruanchuang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.TemplateQuestionOptions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模板问题选项表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface TemplateQuestionOptionsService extends IService<TemplateQuestionOptions> {

    /**
     * 根据多个问题id获取问题id下的选项
     * @param questionIds
     * @return
     */
    Map<Long, List<TemplateQuestionOptions>> getOptionsByQuestionIds(Collection<Long> questionIds);
}
