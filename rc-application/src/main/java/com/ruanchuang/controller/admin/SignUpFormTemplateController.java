package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.AddTemplateDto;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.DeleteByIdsDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpFormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/1/5
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名表管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/template")
public class SignUpFormTemplateController {

    @Autowired
    private SignUpFormTemplateService signUpFormTemplateService;

    @ApiOperation("查询报名表列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(signUpFormTemplateService.getFormList(baseQueryDto));
    }

    @ApiOperation("删除模板")
    @RepeatSubmit
    @DeleteMapping("/delete")
    public CommonResult delete(@Validated @RequestBody DeleteByIdsDto deleteByIdsDto) {
        signUpFormTemplateService.deleteByIds(deleteByIdsDto.getIds());
        return CommonResult.ok();
    }

    @ApiOperation("新增报名表")
    @RepeatSubmit
    @PostMapping("/add")
    public CommonResult add(@Validated @RequestBody AddTemplateDto addTemplateDto) {
        signUpFormTemplateService.add(addTemplateDto);
        return CommonResult.ok();
    }

    @ApiOperation("获取流程列表")
    @GetMapping("/getProcessList")
    public CommonResult processList() {
        return CommonResult.ok(signUpFormTemplateService.getProcessList());
    }

}
