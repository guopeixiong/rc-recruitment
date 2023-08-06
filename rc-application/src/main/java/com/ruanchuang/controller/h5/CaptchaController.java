package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.enums.LimitType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

/**
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
@Api(tags = "获取验证码相关接口")
@Validated
@RestController
@RequestMapping("/h5")
public class CaptchaController {

    @Autowired
    private CodeService codeService;

    /**
     * 接口限流, 针对一个ip一分钟内只能获取一次验证码
     */
    @ApiOperation("获取登录验证码")
    @RateLimiter(key = "getLoginCode", count = 1, limitType = LimitType.IP, message = "一分钟内只能获取一次验证码, 请稍后再试")
    @GetMapping("/getLoginCode/{email}")
    public CommonResult getLoginCode(@Email(message = "邮箱格式错误") @PathVariable("email") String email) {
        codeService.sendCode(email, CacheConstants.CAPTCHA_CODE_KEY_LOGIN);
        return CommonResult.ok();
    }

    /**
     * 接口限流, 针对一个ip一分钟内只能获取一次验证码
     */
    @ApiOperation("获取注册验证码")
    @RateLimiter(key = "getRegisterCode", count = 1, limitType = LimitType.IP, message = "一分钟内只能获取一次验证码, 请稍后再试")
    @GetMapping("/getRegisterCode/{email}")
    public CommonResult getRegisterCode(@Email(message = "邮箱格式错误") @PathVariable("email") String email) {
        codeService.sendCode(email, CacheConstants.CAPTCHA_CODE_KEY_REGISTER);
        return CommonResult.ok();
    }

}
