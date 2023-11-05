package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.domain.SignUpRecordInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.mapper.SignUpRecordInfoMapper;
import com.ruanchuang.service.SignUpRecordInfoService;
import com.ruanchuang.utils.LoginUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报名记录信息表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class SignUpRecordInfoServiceImpl extends ServiceImpl<SignUpRecordInfoMapper, SignUpRecordInfo> implements SignUpRecordInfoService {

    /**
     * 用户分页查询报名记录列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<SignUpRecordInfo> queryUserSignUpRecord(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .eq(SignUpRecordInfo::getUserId, LoginUtils.getLoginUser().getId())
                .select(SignUpRecordInfo::getId,
                        SignUpRecordInfo::getCreateTime,
                        SignUpRecordInfo::getTemplateId)
                .orderByDesc(SignUpRecordInfo::getCreateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

    /**
     * 查询报名详情
     * @param id
     * @return
     */
    @Override
    public Object querySignUpDetail(Long id) {
        return null;
    }

}
