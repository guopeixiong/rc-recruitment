package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruanchuang.domain.TemplateQuestionOptions;
import com.ruanchuang.mapper.TemplateQuestionOptionsMapper;
import com.ruanchuang.service.TemplateQuestionOptionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 模板问题选项表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class TemplateQuestionOptionsServiceImpl extends ServiceImpl<TemplateQuestionOptionsMapper, TemplateQuestionOptions> implements TemplateQuestionOptionsService {

    /**
     * 根据多个问题id获取问题id下的选项, 调用前请先过滤出只包含选择题类型的问题id
     * @param questionIds
     * @return
     */
    @Override
    public Map<Long, List<TemplateQuestionOptions>> getOptionsByQuestionIds(Collection<Long> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            return null;
        }
        List<TemplateQuestionOptions> options = this.baseMapper.selectList(Wrappers.<TemplateQuestionOptions>lambdaQuery()
                .in(TemplateQuestionOptions::getQuestionId, questionIds)
                .select(TemplateQuestionOptions::getId,
                        TemplateQuestionOptions::getQuestionId,
                        TemplateQuestionOptions::getContent
                )
        );
        Map<Long, List<TemplateQuestionOptions>> results = new HashMap<>();
        Iterator<Long> iterator = questionIds.stream().iterator();
        while (iterator.hasNext()) {
            results.put(iterator.next(), new ArrayList<>());
        }
        options.stream().forEach(option ->
            results.get(option.getQuestionId()).add(option)
        );
        return results;
    }
}
