package com.ruanchuang.controller.admin;

import com.ruanchuang.domain.dto.EmailSendRecordQueryDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.EmailSendRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2024/1/17
 * @Email peixiongguo@163.com
 */
@Api(tags = "邮件发送记录相关接口")
@Validated
@RestController
@RequestMapping("/admin/emailSendRecord")
public class EmailSendRecordController {

    @Autowired
    private EmailSendRecordService emailSendRecordService;

    @ApiOperation("邮件发送记录列表")
    @GetMapping("/list")
    public CommonResult list(@Validated EmailSendRecordQueryDto emailSendRecordQueryDto) {
        return CommonResult.ok(emailSendRecordService.queryList(emailSendRecordQueryDto));
    }

}
