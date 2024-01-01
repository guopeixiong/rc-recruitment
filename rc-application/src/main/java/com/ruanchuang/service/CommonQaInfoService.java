package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.CommonQaInfo;
import com.ruanchuang.domain.dto.AddQaDto;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.UpdateQaDto;

import java.util.List;

/**
 * @author guopx
 * @since 2023/11/30
 */
public interface CommonQaInfoService extends IService<CommonQaInfo> {

    /**
     * 获取常见问题
     * @return
     */
    List<CommonQaInfo> getEnableCommonQaInfo();

    /**
     * 后台分页查询常见问题列表
     * @param baseQueryDto
     * @return
     */
    IPage<CommonQaInfo> list(BaseQueryDto baseQueryDto);

    /**
     * 修改常见问题
     * @param updateQaDto
     */
    void updateQa(UpdateQaDto updateQaDto);

    /**
     * 新增常见问题
     * @param addQaDto
     */
    void addQa(AddQaDto addQaDto);
}
