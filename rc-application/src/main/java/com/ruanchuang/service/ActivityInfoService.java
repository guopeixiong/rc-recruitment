package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.ActivityInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SaveOrUpdateActivityDto;

import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/3/16
 * @Email peixiongguo@163.com
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    /**
     * 根据id查询活动信息
     * @param id
     * @return
     */
    ActivityInfo getActivity(Long id);

    /**
     * 查询活动列表
     * @return
     */
    List<ActivityInfo> activityList();

    /**
     * 分页查询活动列表
     * @param baseQueryDto
     * @return
     */
    IPage<ActivityInfo> getList(BaseQueryDto baseQueryDto);

    /**
     * 删除活动
     * @param ids
     */
    void delete(IdsDto ids);

    /**
     * 新增活动
     * @param saveOrUpdateActivityDto
     */
    void add(SaveOrUpdateActivityDto saveOrUpdateActivityDto);

    /**
     * 修改活动
     * @param saveOrUpdateActivityDto
     */
    void edit(SaveOrUpdateActivityDto saveOrUpdateActivityDto);
}
