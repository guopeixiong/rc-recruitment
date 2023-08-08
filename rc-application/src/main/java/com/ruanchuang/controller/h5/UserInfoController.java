package com.ruanchuang.controller.h5;

import com.ruanchuang.domain.SysUser;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.utils.LoginUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2023/8/8
 * @Email peixiongguo@163.com
 */
@Api(tags = "用户信息相关接口")
@RestController
@RequestMapping("/h5/userInfo")
public class UserInfoController {

    @ApiOperation("获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public CommonResult getUserInfo() {
        SysUser user = LoginUtils.getLoginUser();
        LoginUtils.ignoreSensitiveInformation(user);
        return CommonResult.ok(LoginUtils.getLoginUser());
    }

}
