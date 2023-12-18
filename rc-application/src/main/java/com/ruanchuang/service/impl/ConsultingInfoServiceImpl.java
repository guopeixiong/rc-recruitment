package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.ConsultingInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.mapper.ConsultingInfoMapper;
import com.ruanchuang.service.ConsultingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.utils.LoginUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 咨询及回复信息表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
public class ConsultingInfoServiceImpl extends ServiceImpl<ConsultingInfoMapper, ConsultingInfo> implements ConsultingInfoService {

    /**
     * 用户分页查询咨询记录
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<ConsultingInfo> queryConsultingInfoList(BaseQueryDto baseQueryDto) {
        Long id = LoginUtils.getLoginUser().getId();
        return this.lambdaQuery()
                .eq(ConsultingInfo::getUserId, id)
                .orderByDesc(ConsultingInfo::getCreateTime)
                .select(ConsultingInfo::getCreateTime,
                        ConsultingInfo::getContent,
                        ConsultingInfo::getReplyContent,
                        ConsultingInfo::getStatus)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }
}
