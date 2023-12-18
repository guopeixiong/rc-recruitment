package com.ruanchuang.controller.h5;

import com.ruanchuang.annotation.RateLimiter;
import com.ruanchuang.domain.dto.BaseQueryDto;
import com.ruanchuang.enums.LimitType;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.ConsultingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
