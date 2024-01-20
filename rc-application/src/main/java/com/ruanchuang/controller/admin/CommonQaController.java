package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.AddQaDto;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.UpdateQaDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.CommonQaInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2023/12/31
 * @Email peixiongguo@163.com
 */
@Api(tags = "常见问题管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/commonQa")
public class CommonQaController {

    @Autowired
    private CommonQaInfoService commonQaInfoService;

    @ApiOperation("分页查询常见问题列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(commonQaInfoService.list(baseQueryDto));
    }

    @ApiOperation("修改常见问题接口")
    @RepeatSubmit
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "修改常见问题", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public CommonResult edit(@Validated @RequestBody UpdateQaDto updateQaDto) {
        commonQaInfoService.updateQa(updateQaDto);
        return CommonResult.ok();
    }

    @ApiOperation("新增常见问题接口")
    @RepeatSubmit
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "新增常见问题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public CommonResult add(@Validated @RequestBody AddQaDto addQaDto) {
        commonQaInfoService.addQa(addQaDto);
        return CommonResult.ok();
    }

    @ApiOperation("删除常用问题")
    @RepeatSubmit
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "删除常见问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public CommonResult delete(@Validated @RequestBody IdsDto deleteQaDto) {
        commonQaInfoService.deleteQa(deleteQaDto);
        return CommonResult.ok();
    }

}
