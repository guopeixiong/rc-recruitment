package com.ruanchuang.controller.admin;

import com.ruanchuang.domain.dto.LogQueryDto;
import com.ruanchuang.model.CommonResult;
import com.ruanchuang.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author guopeixiong
 * @Date 2024/1/20
 * @Email peixiongguo@163.com
 */
@Api(tags = "日志查询相关接口")
@Validated
@RestController
@RequestMapping("/admin")
public class LogController {

    @Autowired
    private SysLogService sysLogService;

    @ApiOperation("查询日志列表")
    @GetMapping("/userLog")
    public CommonResult userLog(LogQueryDto queryDto) {
        return CommonResult.ok(sysLogService.logList(queryDto));
    }

    @ApiOperation("获取标题列表")
    @GetMapping("/title/list/{type}")
    public CommonResult titleList(@PathVariable(value = "type") Integer type) {
        return CommonResult.ok(sysLogService.titleList(type));
    }

}
