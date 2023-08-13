package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.domain.dto.UpdatePwdDto;
import com.ruanchuang.domain.dto.UpdateUserInfoDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.LimitType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SysUserService;
import com.ruanchuang.utils.LoginUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author guopeixiong
 * @Date 2023/8/8
 * @Email peixiongguo@163.com
 */
@Api(tags = "用户信息相关接口")
@RestController
@RequestMapping("/h5/userInfo")
public class UserInfoController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public CommonResult getUserInfo() {
        SysUser user = LoginUtils.getLoginUser();
        LoginUtils.ignoreSensitiveInformation(user);
        return CommonResult.ok(LoginUtils.getLoginUser());
    }

    @ApiOperation("用户修改个人信息")
    @RepeatSubmit(interval = 10000, message = "重复提交, 请10秒后再试")
    @Log(title = "用户修改个人信息", businessType = BusinessType.UPDATE, saveRequestParam = true)
    @PutMapping("/auth/update")
    public CommonResult updateUserInfo(@Validated @RequestBody UpdateUserInfoDto user) {
        boolean success = sysUserService.updateUserInfo(user);
        return success ? CommonResult.ok() : CommonResult.fail("修改失败, 请稍后再试");
    }

    @ApiOperation("用户修改密码")
    @RepeatSubmit(interval = 10000, message = "重复提交, 请10秒后再试")
    @Log(title = "用户修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/auth/updatePwd")
    public CommonResult updatePwd(@Validated @RequestBody UpdatePwdDto updatePwdDto) {
        sysUserService.updatePwd(updatePwdDto);
        return CommonResult.ok();
    }

    @ApiOperation("用户头像上传")
    @RateLimiter(key = "uploadAvatar", count = 60, limitType = LimitType.DEFAULT, message = "系统繁忙, 请稍后再试")
    @Log(title = "用户修改头像", businessType = BusinessType.UPDATE)
    @PostMapping("/auth/avatar")
    public CommonResult uploadAvatar(@RequestParam("avatarFile") MultipartFile file) {
        String imgUrl = sysUserService.uploadAvatar(file);
        return CommonResult.ok(imgUrl);
    }

}
