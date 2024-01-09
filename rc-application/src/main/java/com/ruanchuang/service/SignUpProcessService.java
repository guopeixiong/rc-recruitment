package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SignUpProcess;
import com.ruanchuang.domain.SignUpProcessStatus;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.DeleteByIdsDto;
import com.ruanchuang.domain.dto.SignUpProcessDto;

import java.util.List;

/**
 * @author guopx
 * @since 2023/12/4
 */
public interface SignUpProcessService extends IService<SignUpProcess> {

    /**
     * 获取初始流程状态id
     * @param processId
     * @return
     */
    Long getDefaultProcessStatusId(Long processId);

    /**
     * 根据状态id获取状态名称
     * @param processStatusId
     * @return
     */
    String getProcessStatusNameById(Long processId, Long processStatusId);

    /**
     * 获取流程状态列表
     * @param processId
     * @return
     */
    List<SignUpProcessStatus> getProcessStatusList(Long processId);

    /**
     * 获取流程列表
     * @param baseQueryDto
     * @return
     */
    IPage<SignUpProcess> getList(BaseQueryDto baseQueryDto);

    /**
     * 删除流程
     * @param deleteByIdsDto
     */
    void deleteByIds(DeleteByIdsDto deleteByIdsDto);

    /**
     * 获取流程状态
     * @param id
     * @return
     */
    List<String> getStatus(Long id);

    /**
     * 新增流程
     * @param signUpProcessDto
     */
    void add(SignUpProcessDto signUpProcessDto);

    /**
     * 编辑流程
     * @param signUpProcessDto
     */
    void edit(SignUpProcessDto signUpProcessDto);
}
