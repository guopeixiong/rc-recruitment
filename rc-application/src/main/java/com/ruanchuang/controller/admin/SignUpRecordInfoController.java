package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.domain.dto.SendEmailDto;
import com.ruanchuang.domain.dto.SignUpRecordQueryDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SignUpFormTemplateService;
import com.ruanchuang.service.SignUpProcessService;
import com.ruanchuang.service.SignUpRecordInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/1/14
 * @Email peixiongguo@163.com
 */
@Api(tags = "报名记录管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/signUpRecord")
public class SignUpRecordInfoController {

    @Autowired
    private SignUpRecordInfoService signUpRecordInfoService;

    @Autowired
    private SignUpProcessService signUpProcessService;

    @Autowired
    private SignUpFormTemplateService signUpFormTemplateService;

    @ApiOperation("获取报名记录列表")
    @GetMapping("/list")
    public CommonResult list(@Validated SignUpRecordQueryDto signUpRecordQueryDto) {
        return CommonResult.ok(signUpRecordInfoService.getList(signUpRecordQueryDto));
    }

    @ApiOperation("获取报名表详情")
    @GetMapping("/detail/{id}")
    public CommonResult detail(@PathVariable("id") Long id) {
        return CommonResult.ok(signUpRecordInfoService.querySignUpDetail(id));
    }

    @ApiOperation("获取报名表列表")
    @GetMapping("/templateList")
    public CommonResult templateList() {
        return CommonResult.ok(signUpFormTemplateService.getList());
    }

    @ApiOperation("获取模板对应流程所有流程状态")
    @GetMapping("/processStatus/{id}")
    public CommonResult processStatus(@PathVariable("id") Long id) {
        return CommonResult.ok(signUpProcessService.getProcessStatusList(id));
    }

    @ApiOperation("将流程进入下一状态")
    @RepeatSubmit
    @Log(title = "变更报名表流程流程", saveRequestParam = true)
    @PutMapping("/nextStatus")
    public CommonResult nextStatus(@Validated @RequestBody IdsDto idsDto) {
        signUpRecordInfoService.nextStatus(idsDto);
        return CommonResult.ok();
    }

    @ApiOperation("结束流程")
    @RepeatSubmit
    @Log(title = "终止报名表状态")
    @PutMapping("/endStatus")
    public CommonResult endStatus(@Validated @RequestBody IdsDto idsDto) {
        signUpRecordInfoService.endStatus(idsDto);
        return CommonResult.ok();
    }

    @ApiOperation("发送邮件通知")
    @RepeatSubmit
    @Log(title = "发送邮件通知")
    @PostMapping("/sendEmail")
    public CommonResult sendEmail(@Validated @RequestBody SendEmailDto sendEmailDto) {
        signUpRecordInfoService.sendEmail(sendEmailDto);
        return CommonResult.ok();
    }

}
