package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.ConsultingInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.SubConsult;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.ConsultingInfoMapper;
import com.ruanchuang.service.ConsultingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 咨询及回复信息表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
@Slf4j
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

    /**
     * 用户提交咨询信息
     * @param subConsult
     */
    @Override
    public void addConsultingInfo(SubConsult subConsult) {
        ConsultingInfo consultingInfo = new ConsultingInfo()
                .setContent(subConsult.getContent())
                .setUserId(LoginUtils.getLoginUser().getId())
                .setStatus(Constants.CONSULTING_INFO_STATUS_UN_REPLY);
        boolean success = this.save(consultingInfo);
        if (!success) {
            log.error("用户提交咨询信息失败, 用户id: {}, 提交内容: {}", LoginUtils.getLoginUser().getId(), subConsult.getContent());
            throw new ServiceException("提交失败, 请稍后再试");
        }
    }
}
