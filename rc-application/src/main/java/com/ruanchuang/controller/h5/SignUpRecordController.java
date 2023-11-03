package com.ruanchuang.controller.h5;

import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpRecordInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2023/9/3
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名记录相关接口")
@Validated
@RestController
@RequestMapping("/h5/signUpRecord")
public class SignUpRecordController {

    @Autowired
    private SignUpRecordInfoService signUpRecordInfoService;

    @ApiOperation("用户分页查询报名记录列表")
    @GetMapping("/auth/getSignUpRecordList/{pageNum}/{pageSize}")
    public CommonResult querySignUpRecordByPage(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(signUpRecordInfoService.queryUserSignUpRecord(baseQueryDto));
    }

}
