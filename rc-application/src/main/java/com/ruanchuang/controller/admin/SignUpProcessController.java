package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SignUpProcessDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @Author guopeixiong
 * @Date 2024/1/8
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名流程管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/process")
public class SignUpProcessController {

    @Autowired
    private SignUpProcessService signUpProcessService;

    @ApiOperation("查询报名流程列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(signUpProcessService.getList(baseQueryDto));
    }

    @ApiOperation("删除报名流程")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "删除报名流程", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("delete")
    public CommonResult delete(@Validated @RequestBody IdsDto deleteByIdsDto) {
        signUpProcessService.deleteByIds(deleteByIdsDto);
        return CommonResult.ok();
    }

    @ApiOperation("查询流程状态列表")
    @GetMapping("/status/{id}")
    public CommonResult status(@NotNull @PathVariable("id") Long id) {
        return CommonResult.ok(signUpProcessService.getStatus(id));
    }

    @ApiOperation("新增报名流程")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "新增报名流程", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/add")
    public CommonResult add(@Validated({SignUpProcessDto.AddGroup.class}) @RequestBody SignUpProcessDto signUpProcessDto) {
        signUpProcessService.add(signUpProcessDto);
        return CommonResult.ok();
    }

    @ApiOperation("修改流程")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "修改报名流程", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/edit")
    public CommonResult edit(@Validated({SignUpProcessDto.UpdateGroup.class}) @RequestBody SignUpProcessDto signUpProcessDto) {
        signUpProcessService.edit(signUpProcessDto);
        return CommonResult.ok();
    }

}
