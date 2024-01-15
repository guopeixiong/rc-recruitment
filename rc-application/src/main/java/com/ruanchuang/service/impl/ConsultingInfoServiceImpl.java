package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.ConsultingInfo;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.ReplyConsultingDto;
import com.ruanchuang.domain.dto.SubConsult;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.ConsultingInfoMapper;
import com.ruanchuang.service.ConsultingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.service.SysUserService;
import com.ruanchuang.utils.EmailUtils;
import com.ruanchuang.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private EmailUtils emailUtils;

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
                        ConsultingInfo::getStatus,
                        ConsultingInfo::getId)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

    /**
     * 回复咨询
     * @param replyDto
     */
    @Override
    public void reply(ReplyConsultingDto replyDto) {
        ConsultingInfo consultingInfo = this.baseMapper.selectOne(Wrappers.<ConsultingInfo>lambdaQuery().eq(ConsultingInfo::getId, replyDto.getId()).select(ConsultingInfo::getUserId,ConsultingInfo::getContent));
        if (Objects.isNull(consultingInfo)) {
            throw new ServiceException("记录不存在");
        }
        consultingInfo.setId(replyDto.getId())
                .setStatus(Constants.CONSULTING_INFO_STATUS_REPLY)
                .setReplyContent(replyDto.getReplyContent());
        boolean success = this.updateById(consultingInfo);
        if (!success) {
            throw new ServiceException("系统异常, 回复失败");
        }
        SysUser user = sysUserService.getBaseMapper().selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getId, consultingInfo.getUserId()).select(SysUser::getEmail));
        emailUtils.sendReplyConsulting(user.getEmail(), consultingInfo.getContent(), consultingInfo.getReplyContent());
    }

    /**
     * 查询咨询人
     * @param userId
     * @return
     */
    @Override
    public SysUser getConsultingPerson(Long userId) {
        return sysUserService.getBaseMapper()
                .selectOne(Wrappers.<SysUser>lambdaQuery()
                        .eq(SysUser::getId, userId)
                        .select(SysUser::getPhone,
                                SysUser::getEmail,
                                SysUser::getNickName,
                                SysUser::getFullName,
                                SysUser::getStuNum,
                                SysUser::getSex));
    }

    /**
     * 删除咨询记录
     * @param deleteByIdsDto
     */
    @Override
    public void delete(IdsDto deleteByIdsDto) {
        boolean success = this.removeBatchByIds(deleteByIdsDto.getIds());
        if (!success) {
            throw new ServiceException("系统异常, 删除失败");
        }
    }

    /**
     * 查询咨询记录列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<ConsultingInfo> getList(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .orderByAsc(ConsultingInfo::getStatus)
                .orderByDesc(ConsultingInfo::getCreateTime)
                .select(ConsultingInfo::getId,
                        ConsultingInfo::getContent,
                        ConsultingInfo::getUserId,
                        ConsultingInfo::getReplyContent,
                        ConsultingInfo::getStatus,
                        ConsultingInfo::getCreateTime,
                        ConsultingInfo::getUpdateBy,
                        ConsultingInfo::getUpdateTime)
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

    /**
     * 查询咨询详情
     * @param id
     * @return
     */
    @Override
    public ConsultingInfo queryConsultingInfoDetail(Long id) {
        return this.baseMapper.selectOne(
                Wrappers.<ConsultingInfo>lambdaQuery()
                        .eq(ConsultingInfo::getId, id)
                        .select(ConsultingInfo::getContent,
                                ConsultingInfo::getReplyContent,
                                ConsultingInfo::getStatus)
        );
    }
}
