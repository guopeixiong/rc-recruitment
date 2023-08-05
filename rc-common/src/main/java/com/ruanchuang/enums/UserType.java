package com.ruanchuang.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
public enum UserType implements IEnum<Integer> {
    /**
     * 普通用户
     */
    AVERAGE_USER(0, "普通用户"),

    /**
     * 系统管理员
     */
    ADMIN(1, "系统管理员");

    private int value;
    private String desc;

    UserType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
