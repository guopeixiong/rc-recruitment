package com.ruanchuang.controller.admin;

import com.ruanchuang.model.CommonResult;
import com.ruanchuang.model.PageDto;
import com.ruanchuang.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/normal/list/{pageNo}/{pageSize}")
    public CommonResult normalList(@Validated PageDto baseQueryDto) {
        return CommonResult.ok(sysUserService.normalList(baseQueryDto));
    }

}
