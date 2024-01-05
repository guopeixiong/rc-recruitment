package com.ruanchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruanchuang.domain.SignUpFormTemplate;
import com.ruanchuang.domain.vo.SignUpFormVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报名表模板表 Mapper 接口
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpFormTemplateMapper extends BaseMapper<SignUpFormTemplate> {

    /**
     * 查询报名表模板列表
     * @param page
     * @return
     */
    IPage<SignUpFormVo> selectFormList(@Param("page") Page page);

}
