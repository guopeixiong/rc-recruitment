package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.ImageBindActivityDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.ActivityInfoService;
import com.ruanchuang.service.IndexRollingImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author guopeixiong
 * @Date 2024/1/28
 * @Email peixiongguo@163.com
 */
@Api(tags = "首页轮播图管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/image")
public class IndexImageController {

    @Autowired
    private IndexRollingImageService indexRollingImageService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("上传图片")
    @RepeatSubmit
    @Log(title = "上传图片", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    @PostMapping("/upload")
    public CommonResult uploadImage(@RequestParam("file") MultipartFile file) {
        return CommonResult.ok(indexRollingImageService.uploadImage(file));
    }

    @ApiOperation("获取图片列表")
    @GetMapping("/list")
    public CommonResult listImage() {
        return CommonResult.ok(indexRollingImageService.listImage());
    }

    @ApiOperation("修改轮播图状态")
    @RepeatSubmit
    @Log(title = "修改轮播图状态", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    @PutMapping("/changeStatus/{id}/{disable}")
    public CommonResult changeStatus(@PathVariable("id") Long id, @PathVariable("disable") Integer disable) {
        indexRollingImageService.changeStatus(id, disable);
        return CommonResult.ok();
    }

    @ApiOperation("删除轮播图")
    @RepeatSubmit
    @Log(title = "删除轮播图", businessType = BusinessType.DELETE, type = Constants.LOG_TYPE_ADMIN)
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteImage(@PathVariable("id") Long id) {
        indexRollingImageService.deleteImage(id);
        return CommonResult.ok();
    }

    @ApiOperation("获取活动列表")
    @GetMapping("/activityList")
    public CommonResult activityList() {
        return CommonResult.ok(activityInfoService.activityList());
    }

    @ApiOperation("绑定活动")
    @PutMapping("/bindActivity")
    @RepeatSubmit
    @Log(title = "轮播图绑定活动", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    public CommonResult bindActivity(@RequestBody @Validated ImageBindActivityDto dto) {
        indexRollingImageService.bindActivity(dto);
        return CommonResult.ok();
    }

}
