package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.CommonQaInfo;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.mapper.CommonQaInfoMapper;
import com.ruanchuang.service.CommonQaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
