package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.ActivityInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SaveOrUpdateActivityDto;
import com.ruanchuang.mapper.ActivityInfoMapper;
import com.ruanchuang.service.ActivityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author guopeixiong
 * @Date 2024/3/16
 * @Email peixiongguo@163.com
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据id查询活动信息
     * @param id
     * @return
     */
    @Override
    public ActivityInfo getActivity(Long id) {
        String redisKey = CacheConstants.ACTIVITY_INFO_CACHE_KEY + id;
        if (redisTemplate.hasKey(redisKey)) {
            return (ActivityInfo) redisTemplate.opsForValue().get(redisKey);
        }
        synchronized (this) {
            if (redisTemplate.hasKey(redisKey)) {
                return (ActivityInfo) redisTemplate.opsForValue().get(redisKey);
            }
            ActivityInfo activity = this.baseMapper.selectOne(Wrappers.<ActivityInfo>lambdaQuery()
                    .eq(ActivityInfo::getId, id)
                    .select(ActivityInfo::getName,
                            ActivityInfo::getContent, ActivityInfo::getTemplateId));
            if (activity == null) {
                return null;
            }
            redisTemplate.opsForValue().set(redisKey, activity, 5, TimeUnit.MINUTES);
            return activity;
        }
    }

    /**
     * 修改活动
     * @param saveOrUpdateActivityDto
     */
    @Override
    public void edit(SaveOrUpdateActivityDto saveOrUpdateActivityDto) {
        boolean success = this.lambdaUpdate()
                .eq(ActivityInfo::getId, saveOrUpdateActivityDto.getId())
                .set(StringUtils.isNotBlank(saveOrUpdateActivityDto.getName()), ActivityInfo::getName, saveOrUpdateActivityDto.getName())
                .set(StringUtils.isNotBlank(saveOrUpdateActivityDto.getRemark()), ActivityInfo::getRemark, saveOrUpdateActivityDto.getRemark())
                .set(ActivityInfo::getTemplateId, saveOrUpdateActivityDto.getTemplateId())
                .set(StringUtils.isNotBlank(saveOrUpdateActivityDto.getContent()), ActivityInfo::getContent, saveOrUpdateActivityDto.getContent())
                .update();
        if (!success) {
            throw new RuntimeException("修改活动失败");
        }
    }

    /**
     * 新增活动
     * @param saveOrUpdateActivityDto
     */
    @Override
    public void add(SaveOrUpdateActivityDto saveOrUpdateActivityDto) {
        ActivityInfo activity = new ActivityInfo();
        activity.setName(saveOrUpdateActivityDto.getName())
                .setRemark(StringUtils.isNotBlank(saveOrUpdateActivityDto.getRemark()) ? saveOrUpdateActivityDto.getRemark() : null)
                .setContent(saveOrUpdateActivityDto.getContent())
                .setTemplateId(saveOrUpdateActivityDto.getTemplateId());
        boolean success = this.save(activity);
        if (!success) {
            throw new RuntimeException("新增活动失败");
        }
    }

    /**
     * 删除活动
     * @param ids
     */
    @Override
    public void delete(IdsDto ids) {
        boolean success = this.removeBatchByIds(ids.getIds());
        if (!success) {
            throw new RuntimeException("删除活动失败");
        }
    }

    /**
     * 分页查询活动列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<ActivityInfo> getList(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .select(ActivityInfo::getId,
                        ActivityInfo::getName,
                        ActivityInfo::getContent,
                        ActivityInfo::getRemark,
                        ActivityInfo::getTemplateId,
                        ActivityInfo::getCreateBy,
                        ActivityInfo::getCreateTime,
                        ActivityInfo::getUpdateBy,
                        ActivityInfo::getUpdateTime)
                .orderByDesc(ActivityInfo::getCreateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

    /**
     * 查询活动列表
     * @return
     */
    @Override
    public List<ActivityInfo> activityList() {
        return this.lambdaQuery()
                .select(ActivityInfo::getId,
                        ActivityInfo::getName)
                .orderByDesc(ActivityInfo::getCreateTime)
                .list();
    }
}
