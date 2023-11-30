package com.ruanchuang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.CommonQaInfo;

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

}
