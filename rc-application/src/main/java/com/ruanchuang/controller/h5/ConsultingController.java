package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.Log;
import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.annotation.RepeatSubmit;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.domain.dto.SubConsult;
import com.ruanchuang.enums.BusinessType;
import com.ruanchuang.enums.LimitType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.ConsultingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author guopx
 * @since 2023/12/18
 */
@Api(tags = "咨询模块相关接口")
@Validated
@RestController
@RequestMapping("/h5/consulting")
public class ConsultingController {

    @Autowired
    private ConsultingInfoService consultingInfoService;

    @ApiOperation("用户分页查询咨询记录")
    @RateLimiter(time = 10, count = 20, limitType = LimitType.IP)
    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public CommonResult consultingInfoList(@Validated BaseQueryDto baseQueryDto) {
        return CommonResult.ok(consultingInfoService.queryConsultingInfoList(baseQueryDto));
    }

    @ApiOperation("用户提交咨询信息")
    @Log(title = "用户提交咨询信息", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @RateLimiter(time = 300, count = 1, limitType = LimitType.IP, message = "五分钟内只能提交一次咨询")
    @PostMapping("/auth/add")
    public CommonResult addConsultingInfo(@Validated @RequestBody SubConsult subConsult) {
        consultingInfoService.addConsultingInfo(subConsult);
        return CommonResult.ok();
    }

}
