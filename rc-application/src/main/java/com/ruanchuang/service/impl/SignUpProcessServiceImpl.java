package com.ruanchuang.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.SignUpProcess;
import com.ruanchuang.domain.SignUpProcessStatus;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SignUpProcessDto;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.exception.SystemException;
import com.ruanchuang.mapper.SignUpProcessMapper;
import com.ruanchuang.service.SignUpProcessService;
import com.ruanchuang.service.SignUpProcessStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                .filter(o -> o.getSortNum() == 0)
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
     * 获取下一个流程状态id
     * @param processId
     * @param processStatusId
     * @return
     */
    @Override
    public Long getNextProcessStatusId(Long processId, Long processStatusId) {
        List<SignUpProcessStatus> statuses = loadStatus(processId);
        for (int i = 0; i < statuses.size(); i++) {
            if (statuses.get(i).getId().equals(processStatusId)) {
                if (i + 1 == statuses.size()) {
                    return processStatusId;
                }
                return statuses.get(i + 1).getId();
            }
        }
        throw new ServiceException("流程状态id不存在");
    }

    /**
     * 编辑流程
     * @param signUpProcessDto
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void edit(SignUpProcessDto signUpProcessDto) {
        SignUpProcess process = new SignUpProcess()
                .setId(signUpProcessDto.getId())
                .setName(signUpProcessDto.getName())
                .setRemark(signUpProcessDto.getRemark())
                .setEnable(signUpProcessDto.getEnable());
        boolean success = this.updateById(process);
        if (!success) {
            throw new SystemException("系统异常, 修改失败");
        }
        if (signUpProcessDto.getStatus().isEmpty()) {
            return;
        }
        success = signUpProcessStatusService.lambdaUpdate()
                .eq(SignUpProcessStatus::getProcessId, signUpProcessDto.getId())
                .remove();
        if (!success) {
            throw new SystemException("系统异常, 修改失败");
        }
        List<SignUpProcessStatus> status = new ArrayList<>(signUpProcessDto.getStatus().size());
        for (int i = 0; i < signUpProcessDto.getStatus().size(); i++) {
            status.add(
                    new SignUpProcessStatus()
                            .setProcessId(signUpProcessDto.getId())
                            .setName(signUpProcessDto.getStatus().get(i))
                            .setSortNum(i)
            );
        }
        success = signUpProcessStatusService.saveBatch(status);
        if (!success) {
            throw new SystemException("系统异常, 修改失败");
        }
    }

    /**
     * 新增流程
     * @param signUpProcessDto
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void add(SignUpProcessDto signUpProcessDto) {
        Snowflake snowflake = IdUtil.getSnowflake();
        SignUpProcess process = new SignUpProcess()
                .setId(snowflake.nextId())
                .setName(signUpProcessDto.getName())
                .setRemark(signUpProcessDto.getRemark())
                .setEnable(signUpProcessDto.getEnable());
        ArrayList<SignUpProcessStatus> status = new ArrayList<>(signUpProcessDto.getStatus().size());
        for (int i = 0; i < signUpProcessDto.getStatus().size(); i++) {
            status.add(
                    new SignUpProcessStatus()
                            .setProcessId(process.getId())
                            .setName(signUpProcessDto.getStatus().get(i))
                            .setSortNum(i)
            );
        }
        boolean success = this.save(process);
        if (!success) {
            throw new SystemException("系统异常, 新增失败");
        }
        success = signUpProcessStatusService.saveBatch(status);
        if (!success) {
            throw new SystemException("系统异常, 新增失败");
        }
    }

    /**
     * 获取流程状态
     * @param id
     * @return
     */
    @Override
    public List<String> getStatus(Long id) {
        return signUpProcessStatusService.lambdaQuery()
                .eq(SignUpProcessStatus::getProcessId, id)
                .orderByAsc(SignUpProcessStatus::getSortNum)
                .select(SignUpProcessStatus::getName)
                .list()
                .stream()
                .map(SignUpProcessStatus::getName)
                .collect(Collectors.toList());
    }

    /**
     * 删除流程
     * @param deleteByIdsDto
     */
    @Override
    public void deleteByIds(IdsDto deleteByIdsDto) {
        boolean success = this.removeBatchByIds(deleteByIdsDto.getIds());
        if (!success) {
            throw new SystemException("系统异常, 删除失败");
        }
    }

    /**
     * 获取流程列表
     * @param baseQueryDto
     * @return
     */
    @Override
    public IPage<SignUpProcess> getList(BaseQueryDto baseQueryDto) {
        return this.lambdaQuery()
                .orderByDesc(SignUpProcess::getEnable, SignUpProcess::getCreateTime)
                .select(SignUpProcess::getId,
                        SignUpProcess::getName,
                        SignUpProcess::getRemark,
                        SignUpProcess::getEnable,
                        SignUpProcess::getCreateBy,
                        SignUpProcess::getCreateTime,
                        SignUpProcess::getUpdateBy,
                        SignUpProcess::getUpdateTime)
                .page(new Page<>(baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
    }

    /**
     * 获取流程状态列表
     * @param processId
     * @return
     */
    @Override
    public List<SignUpProcessStatus> getProcessStatusList(Long processId) {
        List<SignUpProcessStatus> statuses = loadStatus(processId);
        return statuses.stream().sorted(Comparator.comparingInt(SignUpProcessStatus::getSortNum)).collect(Collectors.toList());
    }

    /**
     * 加载流程状态
     *
     * @param processId
     * @return
     */
    private synchronized List<SignUpProcessStatus> loadStatus(Long processId) {
        if (redisTemplate.hasKey(CacheConstants.PROCESS_CACHE_KEY + processId)) {
            return (List<SignUpProcessStatus>) redisTemplate.opsForList().range(CacheConstants.PROCESS_CACHE_KEY + processId, 0, -1).stream().sorted(Comparator.comparing(SignUpProcessStatus::getSortNum)).collect(Collectors.toList());
        }
        List<SignUpProcessStatus> statuses = signUpProcessStatusService.lambdaQuery()
                .eq(SignUpProcessStatus::getProcessId, processId)
                .select(SignUpProcessStatus::getId,
                        SignUpProcessStatus::getSortNum,
                        SignUpProcessStatus::getName,
                        SignUpProcessStatus::getRemark,
                        SignUpProcessStatus::getProcessId)
                .orderByAsc(SignUpProcessStatus::getSortNum)
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
