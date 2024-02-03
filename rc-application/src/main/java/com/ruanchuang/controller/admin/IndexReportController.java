package com.ruanchuang.controller.admin;

import com.ruanchuang.domain.ConsultingInfo;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2024/2/3
 * @Email peixiongguo@163.com
 */
@Api(tags = "后台首页相关接口")
@Validated
@RestController
@RequestMapping("/admin/index")
public class IndexReportController {

    @Autowired
    private SignUpRecordInfoService signUpRecordInfoService;

    @Autowired
    private SysLogService syslogService;

    @Autowired
    private IndexRollingImageService indexRollingImageService;

    @Autowired
    private IndexIntroInfoService indexIntroInfoService;

    @Autowired
    private ConsultingInfoService consultingInfoService;

    @ApiOperation("获取最新100条报名记录")
    @GetMapping("/getLastSignUp")
    public CommonResult getSignUpList() {
        return CommonResult.ok(signUpRecordInfoService.getLastSignUp());
    }

    @ApiOperation("获取最新100条异常日志")
    @GetMapping("/getLastErrorLog")
    public CommonResult getLastErrorLog() {
        return CommonResult.ok(syslogService.lastErrorLog());
    }

    @ApiOperation("获取首页轮播图地址")
    @GetMapping("/getIndexImage")
    public CommonResult getIndexImage() {
        return CommonResult.ok(indexRollingImageService.getIndexImage());
    }

    @ApiOperation("获取首页简介")
    @GetMapping("/getIndexText")
    public CommonResult getIndexText() {
        return CommonResult.ok(indexIntroInfoService.getIndexText());
    }

    @ApiOperation("查询最新100条待回复咨询")
    @GetMapping("/getLastConsulting")
    public CommonResult getLastConsulting() {
        return CommonResult.ok(consultingInfoService.getLastConsulting());
    }

}
