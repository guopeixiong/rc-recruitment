package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SaveOrUpdateActivityDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.ActivityInfoService;
import com.ruanchuang.service.SignUpFormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/3/17
 * @Email peixiongguo@163.com
 */
@Api(tags = "活动管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/activity")
public class ActivityInfoController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private SignUpFormTemplateService signUpFormTemplateService;

    @ApiOperation("获取活动列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult getList(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(activityInfoService.getList(baseQueryDto));
    }

    @ApiOperation("删除活动")
    @DeleteMapping("/delete")
    @RepeatSubmit
    @Log(title = "删除活动信息", businessType = BusinessType.DELETE, type = Constants.LOG_TYPE_ADMIN)
    public CommonResult delete(@RequestBody @Validated IdsDto ids) {
        activityInfoService.delete(ids);
        return CommonResult.ok();
    }

    @ApiOperation("获取报名表列表")
    @GetMapping("/signUpTemplates")
    public CommonResult getSignUpTemplates() {
        return CommonResult.ok(signUpFormTemplateService.getSignUpTemplates());
    }

    @ApiOperation("新增活动")
    @RepeatSubmit
    @Log(title = "新增活动", businessType = BusinessType.INSERT, type = Constants.LOG_TYPE_ADMIN)
    @PostMapping("/add")
    public CommonResult add(@RequestBody @Validated({SaveOrUpdateActivityDto.AddActivity.class}) SaveOrUpdateActivityDto saveOrUpdateActivityDto) {
        activityInfoService.add(saveOrUpdateActivityDto);
        return CommonResult.ok();
    }

    @ApiOperation("获取活动详情")
    @GetMapping("/getDetail/{id}")
    public CommonResult getDetail(@PathVariable("id") Long id) {
        return CommonResult.ok(activityInfoService.getActivity(id));
    }

    @ApiOperation("修改活动信息")
    @RepeatSubmit
    @Log(title = "修改活动信息", businessType = BusinessType.UPDATE, type = Constants.LOG_TYPE_ADMIN)
    @PutMapping("/edit")
    public CommonResult edit(@RequestBody @Validated(SaveOrUpdateActivityDto.UpdateActivity.class) SaveOrUpdateActivityDto saveOrUpdateActivityDto) {
        activityInfoService.edit(saveOrUpdateActivityDto);
        return CommonResult.ok();
    }

}
