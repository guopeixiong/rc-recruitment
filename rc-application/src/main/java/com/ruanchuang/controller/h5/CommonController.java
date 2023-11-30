package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.CommonQaInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guopx
 * @since 2023/11/30
 */
@Api(tags = "h5端通用接口")
@Validated
@RestController
@RequestMapping("/h5")
public class CommonController {

    @Autowired
    private CommonQaInfoService commonQaInfoService;

    @ApiOperation("获取常见问题接口")
    @RateLimiter(key = "getForm", count = 1000, message = "服务器限流, 请稍后再试")
    @GetMapping("/getCommonQaInfo")
    public CommonResult getCommonQaInfo() {
        return CommonResult.ok(commonQaInfoService.getEnableCommonQaInfo());
    }

}
