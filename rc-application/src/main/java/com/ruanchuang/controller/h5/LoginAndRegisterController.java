package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.LoginDto;
import com.ruanchuang.domain.dto.RegisterDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author guopeixiong
 * @Date 2023/8/3
 * @Email peixiongguo@163.com
 */
@Api(tags = "登录注册相关接口")
@RestController
@RequestMapping("/h5")
public class LoginAndRegisterController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("账号密码登录")
    @RepeatSubmit(interval = 6000)
    @PostMapping("/login")
    public CommonResult login(@Validated({LoginDto.LoginByPhoneOrStuNum.class}) @RequestBody LoginDto loginDto, HttpServletRequest request) {
        return CommonResult.ok(sysUserService.loginByPhoneAndPassword(loginDto, request));
    }

    @ApiOperation("邮箱验证码登录")
    @RepeatSubmit(interval = 6000)
    @PostMapping("/loginByCode")
    public CommonResult loginByEmailCode(@Validated({LoginDto.LoginByEmail.class}) @RequestBody LoginDto loginDto, HttpServletRequest request) {
        return CommonResult.ok(sysUserService.loginByEmailCode(loginDto, request));
    }

    @ApiOperation("用户注册")
    @RepeatSubmit(interval = 6000)
    @Log(title = "用户注册", businessType = BusinessType.INSERT, saveRequestParam = true)
    @PostMapping("/register")
    public CommonResult userRegister(@Validated @RequestBody RegisterDto registerDto) {
        sysUserService.userRegister(registerDto);
        return CommonResult.ok();
    }

}
