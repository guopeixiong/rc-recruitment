package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.CommonQaInfo;
import com.ruanchuang.domain.dto.AddQaDto;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.DeleteByIdsDto;
import com.ruanchuang.domain.dto.UpdateQaDto;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.CommonQaInfoMapper;
import com.ruanchuang.service.CommonQaInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author guopx
 * @since 2023/11/30
 */
@Service
public class CommonQaInfoServiceImpl extends ServiceImpl<CommonQaInfoMapper, CommonQaInfo> implements CommonQaInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取常见问题
     * @return
     */
    @Override
    public List<CommonQaInfo> getEnableCommonQaInfo() {
        if (redisTemplate.hasKey(CacheConstants.COMMON_QA_INFO_CACHE_KEY)) {
            return redisTemplate.opsForList().range(CacheConstants.COMMON_QA_INFO_CACHE_KEY, 0, -1);
        }
        synchronized (this) {
            if (redisTemplate.hasKey(CacheConstants.COMMON_QA_INFO_CACHE_KEY)) {
                return redisTemplate.opsForList().range(CacheConstants.COMMON_QA_INFO_CACHE_KEY, 0, -1);
            }
            List<CommonQaInfo> qaInfos = this.lambdaQuery()
                    .eq(CommonQaInfo::getEnable, Constants.COMMON_QA_INFO_STATUS_ENABLE)
                    .orderByDesc(CommonQaInfo::getTop, CommonQaInfo::getCreateTime)
                    .select(CommonQaInfo::getQuestion,
                            CommonQaInfo::getAnswer,
                            CommonQaInfo::getTop)
                    .list();
            if (qaInfos.isEmpty()) {
                return List.of();
            }
            redisTemplate.opsForList().rightPushAll(CacheConstants.COMMON_QA_INFO_CACHE_KEY, qaInfos);
            // 只缓存5分钟, 过期再次获取
            redisTemplate.expire(CacheConstants.COMMON_QA_INFO_CACHE_KEY, 5, TimeUnit.MINUTES);
            return qaInfos;
        }
    }

    /**
     * 删除常见问题
     * @param deleteQaDto
     */
    @Override
    public void deleteQa(DeleteByIdsDto deleteQaDto) {
        boolean success = this.removeByIds(deleteQaDto.getIds());
        if (!success) {
            throw new ServiceException("删除失败，请稍后再试");
        }
    }

    /**
     * 新增常见问题
     * @param addQaDto
     */
    @Override
    public void addQa(AddQaDto addQaDto) {
        CommonQaInfo commonQaInfo = new CommonQaInfo()
                .setQuestion(addQaDto.getQuestion())
                .setAnswer(addQaDto.getAnswer())
                .setRemark(addQaDto.getRemark())
                .setEnable(addQaDto.getEnable())
                .setTop(addQaDto.getTop());
        boolean success = this.save(commonQaInfo);
        if (!success) {
            throw new ServiceException("新增失败，请稍后再试");
        }
        redisTemplate.delete(CacheConstants.COMMON_QA_INFO_CACHE_KEY);
    }

    /**
     * 修改常见问题
     * @param updateQaDto
     */
    @Override
    public void updateQa(UpdateQaDto updateQaDto) {
        boolean success = this.lambdaUpdate()
                .eq(CommonQaInfo::getId, updateQaDto.getId())
                .set(StringUtils.isNotBlank(updateQaDto.getQuestion()), CommonQaInfo::getQuestion, updateQaDto.getQuestion())
                .set(StringUtils.isNotBlank(updateQaDto.getAnswer()), CommonQaInfo::getAnswer, updateQaDto.getAnswer())
                .set(StringUtils.isNotBlank(updateQaDto.getRemark()), CommonQaInfo::getRemark, updateQaDto.getRemark())
                .set(Objects.nonNull(updateQaDto.getTop()), CommonQaInfo::getTop, updateQaDto.getTop())
                .set(Objects.nonNull(updateQaDto.getEnable()), CommonQaInfo::getEnable, updateQaDto.getEnable())
                .update();
        if (!success) {
            throw new ServiceException("修改失败，请稍后再试");
        }
        redisTemplate.delete(CacheConstants.COMMON_QA_INFO_CACHE_KEY);
    }

    /**
     * 后台分页查询常见问题列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<CommonQaInfo> list(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .orderByDesc(CommonQaInfo::getTop)
                .orderByDesc(CommonQaInfo::getEnable)
                .orderByDesc(CommonQaInfo::getCreateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

}
