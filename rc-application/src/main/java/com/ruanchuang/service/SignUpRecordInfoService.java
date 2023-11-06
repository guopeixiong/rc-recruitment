package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SignUpRecordInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.vo.SignUpDetailVo;

import java.util.List;

/**
 * <p>
 * 报名记录信息表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpRecordInfoService extends IService<SignUpRecordInfo> {

    /**
     * 用户分页查询报名记录列表
     * @param baseQueryDto
     * @return
     */
    IPage<SignUpRecordInfo> queryUserSignUpRecord(BaseQueryDto baseQueryDto);

    /**
     * 查询报名记录详情
     * @param id
     * @return
     */
    List<SignUpDetailVo> querySignUpDetail(Long id);
}
