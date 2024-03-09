package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.IndexIntroInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.IndexIntroInfoMapper;
import com.ruanchuang.service.IndexIntroInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author guopeixiong
 * @Date 2024/1/21
 * @Email peixiongguo@163.com
 */
@Service
public class IndexIntroInfoServiceImpl extends ServiceImpl<IndexIntroInfoMapper, IndexIntroInfo> implements IndexIntroInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 删除简介记录
     * @param idsDto
     */
    @Override
    public void delete(IdsDto idsDto) {
        boolean success = this.removeBatchByIds(idsDto.getIds());
        if (!success) {
            throw new ServiceException("系统异常，删除失败");
        }
    }

    /**
     * 获取首页简介
     * @return
     */
    @Override
    public String getIndexText() {
        if (redisTemplate.hasKey(CacheConstants.INDEX_INTRO_CACHE_KEY)) {
            return (String) redisTemplate.opsForValue().get(CacheConstants.INDEX_INTRO_CACHE_KEY);
        }
        synchronized (this) {
            if (redisTemplate.hasKey(CacheConstants.INDEX_INTRO_CACHE_KEY)) {
                return (String) redisTemplate.opsForValue().get(CacheConstants.INDEX_INTRO_CACHE_KEY);
            }
            IndexIntroInfo info = this.baseMapper.selectOne(Wrappers.<IndexIntroInfo>lambdaQuery().eq(IndexIntroInfo::getEnable, Constants.INDEX_INTRO_STATUS_ENABLE).select(IndexIntroInfo::getContent));
            if (Objects.isNull(info) && Objects.isNull(info.getContent())) {
                info.setContent("");
            }
            redisTemplate.opsForValue().set(CacheConstants.INDEX_INTRO_CACHE_KEY, info.getContent(), 5, TimeUnit.MINUTES);
            return Objects.isNull(info) ? "" : info.getContent();
        }
    }

    /**
     * 更新简介记录
     * @param indexIntroInfoDto
     */
    @Override
    public void updateInfo(IndexIntroInfo indexIntroInfoDto) {
        if (indexIntroInfoDto.getId() == null) {
            throw new ServiceException("系统异常，id为空");
        }
        IndexIntroInfo updateInfo = new IndexIntroInfo();
        updateInfo.setId(indexIntroInfoDto.getId())
                .setTitle(indexIntroInfoDto.getTitle())
                .setRemark(indexIntroInfoDto.getRemark())
                .setContent(indexIntroInfoDto.getContent());
        boolean success = this.updateById(updateInfo);
        if (!success) {
            throw new ServiceException("系统异常，更新失败");
        }
        redisTemplate.delete(CacheConstants.INDEX_INTRO_CACHE_KEY);
    }

    /**
     * 获取简介详情
     * @param id
     * @return
     */
    @Override
    public String detailContent(Long id) {
        IndexIntroInfo info = this.getById(id);
        if (Objects.isNull(info)) {
            throw new ServiceException("记录不存在");
        }
        return info.getContent();
    }

    /**
     * 添加简介记录
     * @param indexIntroInfoDto
     */
    @Override
    public void add(IndexIntroInfo indexIntroInfoDto) {
        if (StringUtils.isBlank(indexIntroInfoDto.getTitle())) {
            throw new ServiceException("标题不能为空");
        }
        IndexIntroInfo indexIntroInfo = new IndexIntroInfo()
                .setTitle(indexIntroInfoDto.getTitle());
        if (StringUtils.isNotBlank(indexIntroInfo.getRemark())) {
            indexIntroInfo.setRemark(indexIntroInfo.getRemark());
        }
        boolean success = this.save(indexIntroInfo);
        if (!success) {
            throw new ServiceException("系统异常，添加失败");
        }
        redisTemplate.delete(CacheConstants.INDEX_INTRO_CACHE_KEY);
    }

    /**
     * 启用简介记录
     * @param id
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void enable(Long id) {
        boolean success = this.lambdaUpdate()
                .eq(IndexIntroInfo::getEnable, Constants.INDEX_INTRO_STATUS_ENABLE)
                .set(IndexIntroInfo::getEnable, Constants.INDEX_INTRO_STATUS_DISABLE)
                .update();
        if (!success) {
            throw new ServiceException("系统异常，启用失败");
        }
        success = this.lambdaUpdate()
                .eq(IndexIntroInfo::getId, id)
                .set(IndexIntroInfo::getEnable, Constants.INDEX_INTRO_STATUS_ENABLE)
                .update();
        if (!success) {
            throw new ServiceException("系统异常，启用失败");
        }
        redisTemplate.delete(CacheConstants.INDEX_INTRO_CACHE_KEY);
    }

    /**
     * 获取简介记录列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<IndexIntroInfo> getList(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .orderByDesc(IndexIntroInfo::getEnable, IndexIntroInfo::getCreateTime)
                .select(IndexIntroInfo::getId,
                        IndexIntroInfo::getTitle,
                        IndexIntroInfo::getRemark,
                        IndexIntroInfo::getEnable,
                        IndexIntroInfo::getCreateBy,
                        IndexIntroInfo::getCreateTime,
                        IndexIntroInfo::getUpdateBy,
                        IndexIntroInfo::getUpdateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

}
