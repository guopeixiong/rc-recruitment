package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpFormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2023/8/19
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名表模块相关接口")
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

}
