package com.ruanchuang.controller.admin;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.EmailTemplate;
import com.ruanchuang.domain.dto.AddEmailTemplateDTO;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.IdsDto;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.EmailTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author guopeixiong
 * @Date 2024/1/16
 * @Email peixiongguo@163.com
 */
@Api(tags = "邮件模板管理相关接口")
@Validated
@RestController
@RequestMapping("/admin/emailTemplate")
public class EmailTemplateController {

    @Autowired
    private EmailTemplateService emailTemplateService;

    @ApiOperation("新增邮件模板")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "新增邮件模板", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/add")
    public CommonResult add(@Validated @RequestBody AddEmailTemplateDTO addEmailTemplateDTO) {
        emailTemplateService.addTemplate(addEmailTemplateDTO);
        return CommonResult.ok();
    }

    @ApiOperation("邮件模板列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public CommonResult list(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(emailTemplateService.getList(baseQueryDto));
    }

    @ApiOperation("删除邮件模板")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "删除邮件模板", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/delete")
    public CommonResult delete(@Validated @RequestBody IdsDto idsDto) {
        emailTemplateService.deleteTemplate(idsDto);
        return CommonResult.ok();
    }

    @ApiOperation("修改邮件模板")
    @Log(type = Constants.LOG_TYPE_ADMIN, title = "修改邮件模板", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/update")
    public CommonResult update(@Validated @RequestBody EmailTemplate emailTemplate) {
        emailTemplateService.updateTemplate(emailTemplate);
        return CommonResult.ok();
    }

}
