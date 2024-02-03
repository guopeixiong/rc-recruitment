package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.IndexIntroInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;

/**
 * @Author guopeixiong
 * @Date 2024/1/21
 * @Email peixiongguo@163.com
 */
public interface IndexIntroInfoService extends IService<IndexIntroInfo> {

    /**
     * 获取简介记录列表
     * @param baseQueryDto
     * @return
     */
    IPage<IndexIntroInfo> getList(BaseQueryDto baseQueryDto);

    /**
     * 删除简介记录
     * @param idsDto
     */
    void delete(IdsDto idsDto);

    /**
     * 启用简介记录
     * @param id
     */
    void enable(Long id);

    /**
     * 添加简介记录
     * @param indexIntroInfoDto
     */
    void add(IndexIntroInfo indexIntroInfoDto);

    /**
     * 获取简介详情
     * @param id
     * @return
     */
    String detailContent(Long id);

    /**
     * 更新简介记录
     * @param indexIntroInfoDto
     */
    void updateInfo(IndexIntroInfo indexIntroInfoDto);

    /**
     * 获取首页简介
     * @return
     */
    String getIndexText();
}
