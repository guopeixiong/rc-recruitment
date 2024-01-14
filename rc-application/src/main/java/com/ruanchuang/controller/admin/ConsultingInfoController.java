package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.DeleteByIdsDto;
import com.ruanchuang.domain.dto.ReplyConsultingDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.ConsultingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/1/13
 * @Email peixiongguo@163.com
 */
@Api(tags = "咨询管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/consulting")
public class ConsultingInfoController {

    @Autowired
    private ConsultingInfoService consultingInfoService;

    @ApiOperation("查询咨询问题列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(consultingInfoService.getList(baseQueryDto));
    }

    @ApiOperation("删除咨询问题")
    @RepeatSubmit
    @Log(title = "删除咨询问题")
    @DeleteMapping("/delete")
    public CommonResult delete(@Validated @RequestBody DeleteByIdsDto deleteByIdsDto) {
        consultingInfoService.delete(deleteByIdsDto);
        return CommonResult.ok();
    }

    @ApiOperation("查询咨询问题对应用户信息")
    @GetMapping("/getConsultingPerson/{userId}")
    public CommonResult getConsultingPerson(@PathVariable("userId") Long userId) {
        return CommonResult.ok(consultingInfoService.getConsultingPerson(userId));
    }

    @ApiOperation("回复咨询")
    @RepeatSubmit
    @Log(title = "回复咨询问题")
    @PutMapping("/reply")
    public CommonResult reply(@Validated @RequestBody ReplyConsultingDto replyDto) {
        consultingInfoService.reply(replyDto);
        return CommonResult.ok();
    }

}
