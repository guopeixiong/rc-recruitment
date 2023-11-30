package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.UpdateSignUpFormDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpFormTemplateService;
import com.ruanchuang.service.SignUpRecordInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SignUpFormTemplateService signUpFormTemplateService;



    @ApiOperation("用户分页查询报名记录列表")
    @GetMapping("/auth/getSignUpRecordList/{pageNum}/{pageSize}")
    public CommonResult querySignUpRecordByPage(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(signUpRecordInfoService.queryUserSignUpRecord(baseQueryDto));
    }

    @ApiOperation("用户查询报名表详情")
    @GetMapping("/auth/getSignUpDetail/{id}")
    public CommonResult querySignUpDetail(@PathVariable("id") Long id) {
        return CommonResult.ok(signUpRecordInfoService.querySignUpDetail(id));
    }

    @ApiOperation("用户查询问题剩余修改次数")
    @GetMapping("/auth/getTheRestOfQuestionUpdateTimes/{id}")
    public CommonResult getTheRestOfQuestionUpdateTimes(@PathVariable("id") Long id) {
        return CommonResult.ok(signUpFormTemplateService.queryTheRestOfQuestionUpdateTimes(id));
    }

    @ApiOperation("用户修改报名表问题")
    @RepeatSubmit(interval = 10000, message = "重复提交, 请10秒后再试")
    @Log(title = "用户修改报名表问题", businessType = BusinessType.UPDATE)
    @PutMapping("/auth/update")
    public CommonResult updateForm(@Validated @RequestBody UpdateSignUpFormDto updateSignUpFormDto) {
        signUpFormTemplateService.updateForm(updateSignUpFormDto);
        return CommonResult.ok();
    }

    @ApiOperation("查询选择题选项")
    @GetMapping("/auth/getChoiceQuestion/{id}")
    public CommonResult queryChoiceQuestion(@PathVariable("id") Long id) {
        return CommonResult.ok(signUpFormTemplateService.queryChoiceQuestion(id));
    }

}
