package com.ruanchuang.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 操作状态
 *
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
public enum BusinessStatus implements IEnum<Integer> {
    /**
     * 成功
     */
    SUCCESS(0, "正常"),

    /**
     * 失败
     */
    FAIL(1, "失败");

    private int value;
    private String desc;

    BusinessStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
