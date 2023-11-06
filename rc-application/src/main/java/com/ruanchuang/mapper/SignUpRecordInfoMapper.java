package com.ruanchuang.mapper;

import com.ruanchuang.domain.SignUpRecordInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruanchuang.domain.vo.SignUpDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 报名记录信息表 Mapper 接口
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface SignUpRecordInfoMapper extends BaseMapper<SignUpRecordInfo> {

    /**
     * 查询报名详情
     *
     * @param id         报名记录id
     * @param templateId 模板id
     * @param userId     用户id
     * @return 报名详情
     */
    List<SignUpDetailVo> querySignUpDetail(@Param("id") Long id, @Param("templateId") Long templateId, @Param("userId") Long userId);
}
