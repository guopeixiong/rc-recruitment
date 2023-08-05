package com.ruanchuang.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 业务操作类型
 *
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
public enum BusinessType implements IEnum<Integer> {
    /**
     * 其它
     */
    OTHER(0, "其他"),

    /**
     * 新增
     */
    INSERT(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 导出
     */
    EXPORT(4, "导出"),

    /**
     * 导入
     */
    IMPORT(5, "导入"),

    /**
     * 登录
     */
    LOGIN(6, "登录");

    private int value;
    private String desc;

    BusinessType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
