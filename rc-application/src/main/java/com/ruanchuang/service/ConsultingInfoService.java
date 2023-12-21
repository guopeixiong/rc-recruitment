package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.ConsultingInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.SubConsult;

/**
 * <p>
 * 咨询及回复信息表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface ConsultingInfoService extends IService<ConsultingInfo> {

    /**
     * 用户分页查询咨询记录
     * @param baseQueryDto
     * @return
     */
    IPage<ConsultingInfo> queryConsultingInfoList(BaseQueryDto baseQueryDto);

    /**
     * 用户提交咨询信息
     * @param subConsult
     */
    void addConsultingInfo(SubConsult subConsult);

    /**
     * 查询咨询详情
     * @param id
     * @return
     */
    ConsultingInfo queryConsultingInfoDetail(Long id);
}
