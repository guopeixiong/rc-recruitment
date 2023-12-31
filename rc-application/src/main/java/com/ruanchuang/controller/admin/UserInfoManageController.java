package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.AddAdminDto;
import com.ruanchuang.domain.dto.UserQueryDto;
import com.ruanchuang.domain.dto.UserStatusDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2023/12/23
 * @Email peixiongguo@163.com
 */
@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/admin/user")
public class UserInfoManageController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("分页查询普通用户")
    @GetMapping("/normal/list")
    public CommonResult normalList(@Validated UserQueryDto userQueryDto) {
        return CommonResult.ok(sysUserService.normalList(userQueryDto));
    }

    @ApiOperation("禁用普通用户")
    @Log(title = "修改用户账号状态", businessType = BusinessType.UPDATE, saveRequestParam = true)
    @PutMapping("/normal/status")
    public CommonResult normalDisAble(@RequestBody UserStatusDto userStatusDto) {
        sysUserService.updateUserStatus(userStatusDto);
        return CommonResult.ok();
    }

    @ApiOperation("分页查询管理员用户")
    @GetMapping("/admin/list")
    public CommonResult adminList(@Validated UserQueryDto userQueryDto) {
        return CommonResult.ok(sysUserService.adminList(userQueryDto));
    }

    @ApiOperation("新增管理员")
    @RepeatSubmit
    @Log(title = "新增管理员", businessType = BusinessType.INSERT, saveRequestParam = true, saveResponseResult = true)
    @PostMapping("/admin/add")
    public CommonResult addAdmin(@Validated @RequestBody AddAdminDto addAdminDto) {
        sysUserService.addAdmin(addAdminDto);
        return CommonResult.ok();
    }

}
