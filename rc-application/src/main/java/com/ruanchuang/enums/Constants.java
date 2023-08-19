package com.ruanchuang.enums;

/**
 * 常用常量值
 * @Author guopeixiong
 * @Date 2023/8/19
 * @Email peixiongguo@163.com
 */
public interface Constants {

    /**
     * 报名表模板状态 启动
     */
    Integer SIGN_UP_FORM_TEMPLATE_STATUS_ENABLE = 1;

    /**
     * 报名表模板状态 未启动
     */
    Integer SIGN_UP_FORM_TEMPLATE_STATUS_DISABLE = 0;

    /**
     * 报名表问题类型 填空题
     */
    Integer SIGN_UP_FORM_QUESTION_TYPE_TEXT = 0;

    /**
     * 报名表问题类型 单选题
     */
    Integer SIGN_UP_FORM_QUESTION_TYPE_SINGLE_CHOICE = 1;

    /**
     * 报名表问题类型 多选题
     */
    Integer SIGN_UP_FORM_QUESTION_TYPE_MULTIPLE_CHOICE = 2;

}
