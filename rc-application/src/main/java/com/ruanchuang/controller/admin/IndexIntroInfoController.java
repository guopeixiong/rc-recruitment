package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.IndexIntroInfo;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.IndexIntroInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/1/21
 * @Email peixiongguo@163.com
 */
@Api(tags = "首页简介相关接口")
@Validated
@RestController
@RequestMapping("/admin/indexIntro")
public class IndexIntroInfoController {

    @Autowired
    private IndexIntroInfoService indexIntroInfoService;

    @ApiOperation("简介记录列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(indexIntroInfoService.getList(baseQueryDto));
    }

    @ApiOperation("删除记录")
    @DeleteMapping("/delete")
    @RepeatSubmit
    @Log(title = "删除首页简介", businessType = BusinessType.DELETE, type = Constants.LOG_TYPE_ADMIN)
    public CommonResult delete(@Validated @RequestBody IdsDto idsDto) {
        indexIntroInfoService.delete(idsDto);
        return CommonResult.ok();
    }

    @ApiOperation("启用首页简介")
    @RepeatSubmit
    @Log(title = "启用首页简介", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    @PutMapping("/enable/{id}")
    public CommonResult enable(@PathVariable("id") Long id) {
        indexIntroInfoService.enable(id);
        return CommonResult.ok();
    }

    @ApiOperation("新增首页简介")
    @RepeatSubmit
    @PostMapping("/add")
    @Log(title = "新增首页简介", businessType = BusinessType.INSERT, type = Constants.LOG_TYPE_ADMIN)
    public CommonResult add(@RequestBody IndexIntroInfo indexIntroInfoDto) {
        indexIntroInfoService.add(indexIntroInfoDto);
        return CommonResult.ok();
    }

    @ApiOperation("获取简介内容")
    @GetMapping("/detail/{id}")
    public CommonResult detailContent(@PathVariable("id") Long id) {
        return CommonResult.ok(indexIntroInfoService.detailContent(id));
    }

    @ApiOperation("修改简介信息")
    @RepeatSubmit
    @Log(title = "修改首页简介", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    @PutMapping("/update")
    public CommonResult update(@RequestBody IndexIntroInfo indexIntroInfoDto) {
        indexIntroInfoService.updateInfo(indexIntroInfoDto);
        return CommonResult.ok();
    }

}
