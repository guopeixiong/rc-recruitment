package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.LoginDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author guopeixiong
 * @Date 2023/8/3
 * @Email peixiongguo@163.com
 */
@Api(tags = "登录相关接口")
@RestController
public class LoginController {

    @Autowired
    private SysUserService userService;

    @ApiOperation("登录接口")
    @RepeatSubmit
    @PostMapping("/login")
    public CommonResult login(@Validated({LoginDto.LoginByPhoneOrStuNum.class}) @RequestBody LoginDto loginDto, HttpServletRequest request) {
        return CommonResult.ok(userService.adminLogin(loginDto, request));
    }

}
