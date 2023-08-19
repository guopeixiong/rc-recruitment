package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SignUpFormQuestion;
import com.ruanchuang.domain.TemplateQuestionOptions;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.mapper.SignUpFormQuestionMapper;
import com.ruanchuang.service.SignUpFormQuestionService;
import com.ruanchuang.service.TemplateQuestionOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 报名表问题表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class SignUpFormQuestionServiceImpl extends ServiceImpl<SignUpFormQuestionMapper, SignUpFormQuestion> implements SignUpFormQuestionService {

    @Autowired
    private TemplateQuestionOptionsService templateQuestionOptionsService;

    /**
     * 根据模板id查询问题列表
     *
     * @param id
     * @return
     */
    @Override
    public List<SignUpFormQuestion> selectQuestionsByTemplateId(Long id) {
        List<SignUpFormQuestion> questions = this.baseMapper.selectList(
                Wrappers.<SignUpFormQuestion>lambdaQuery()
                        .eq(SignUpFormQuestion::getTemplateId, id)
                        .orderByAsc(SignUpFormQuestion::getSort)
                        .select(
                                SignUpFormQuestion::getId,
                                SignUpFormQuestion::getTemplateId,
                                SignUpFormQuestion::getContent,
                                SignUpFormQuestion::getType,
                                SignUpFormQuestion::getIsRequire
                        )
        );
        Set<Long> questionIds = questions.stream()
                .filter(question -> question.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE) || question.getType().equals(Constants.SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE))
                .map(SignUpFormQuestion::getId)
                .collect(Collectors.toSet());
        if (questionIds.isEmpty()) {
            return questions;
        }
        Map<Long, List<TemplateQuestionOptions>> options = templateQuestionOptionsService.getOptionsByQuestionIds(questionIds);
        questions.stream().filter(question -> questionIds.contains(question.getId()))
                .forEach(question -> question.setOptions(options.get(question.getId())));
        return questions;
    }
}
