package com.ruanchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.SignUpProcess;
import com.ruanchuang.domain.SignUpProcessStatus;
import com.ruanchuang.exception.SystemException;
import com.ruanchuang.mapper.SignUpProcessMapper;
import com.ruanchuang.service.SignUpProcessService;
import com.ruanchuang.service.SignUpProcessStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author guopx
 * @since 2023/12/4
 */
@Service
public class SignUpProcessServiceImpl extends ServiceImpl<SignUpProcessMapper, SignUpProcess> implements SignUpProcessService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SignUpProcessStatusService signUpProcessStatusService;

    /**
     * 获取初始流程状态id
     *
     * @param processId
     * @return
     */
    @Override
    public Long getDefaultProcessStatusId(Long processId) {
        return loadStatus(processId).stream()
                .filter(o -> Objects.isNull(o.getNextStatusId()))
                .findFirst()
                .map(SignUpProcessStatus::getId)
                .get();
    }

    /**
     * 根据状态id获取状态名称
     * @param processStatusId
     * @return
     */
    @Override
    public String getProcessStatusNameById(Long processId, Long processStatusId) {
        return loadStatus(processId).stream()
                .filter(o -> o.getId().equals(processStatusId))
                .findFirst()
                .map(SignUpProcessStatus::getName)
                .get();
    }

    /**
     * 获取流程状态列表
     * @param processId
     * @return
     */
    @Override
    public List<SignUpProcessStatus> getProcessStatusList(Long processId) {
        List<SignUpProcessStatus> statuses = loadStatus(processId);
        Map<Long, SignUpProcessStatus> statusMap = new HashMap<>();
        statuses.forEach(o -> statusMap.put(o.getId(), o));
        List<SignUpProcessStatus> statusList = new ArrayList<>(statuses.size());
        Long nextId = getDefaultProcessStatusId(processId);
        while (Objects.isNull(nextId)) {
            statusList.add(statusMap.get(nextId));
            nextId = statusMap.get(nextId).getNextStatusId();
        }
        return statusList;
    }

    /**
     * 加载流程状态
     *
     * @param processId
     * @return
     */
    private synchronized List<SignUpProcessStatus> loadStatus(Long processId) {
        if (redisTemplate.hasKey(CacheConstants.PROCESS_CACHE_KEY + processId)) {
            return redisTemplate.opsForList().range(CacheConstants.PROCESS_CACHE_KEY + processId, 0, -1);
        }
        List<SignUpProcessStatus> statuses = signUpProcessStatusService.lambdaQuery()
                .eq(SignUpProcessStatus::getProcessId, processId)
                .select(SignUpProcessStatus::getId,
                        SignUpProcessStatus::getNextStatusId,
                        SignUpProcessStatus::getName,
                        SignUpProcessStatus::getRemark,
                        SignUpProcessStatus::getProcessId)
                .list();
        if (statuses.isEmpty()) {
            throw new SystemException("流程不存在具体状态值");
        }
        redisTemplate.opsForList().leftPushAll(CacheConstants.PROCESS_CACHE_KEY + processId, statuses);
        // 缓存五分钟, 五分钟后重新读取
        redisTemplate.expire(CacheConstants.PROCESS_CACHE_KEY + processId, 5, TimeUnit.MINUTES);
        return statuses;
    }

}
