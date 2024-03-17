package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.ActivityInfo;
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
