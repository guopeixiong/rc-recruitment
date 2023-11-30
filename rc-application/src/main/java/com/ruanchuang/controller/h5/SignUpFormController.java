package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.SubmitFormDto;
import com.ruanchuang.domain.dto.UpdateSignUpFormDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpFormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2023/8/19
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名表模块相关接口")
@Validated
@RestController
@RequestMapping("/h5/signUp")
public class SignUpFormController {

    @Autowired
    private SignUpFormTemplateService signUpFormTemplateService;

    @ApiOperation("获取报名表接口")
    @RateLimiter(key = "getForm", count = 1000, message = "服务器限流, 请稍后再试")
    @GetMapping("/getForm")
    public CommonResult getForm() {
        return CommonResult.ok(signUpFormTemplateService.getForm());
    }

    @ApiOperation("用户提交报名表")
    @RepeatSubmit
    @Log(title = "用户提交报名表", businessType = BusinessType.INSERT)
    @PostMapping("/auth/submit")
    public CommonResult submitForm(@Validated @RequestBody List<SubmitFormDto> formDto) {
        signUpFormTemplateService.submitForm(formDto);
        return CommonResult.ok();
    }

}
