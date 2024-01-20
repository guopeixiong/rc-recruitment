package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/1/20
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("日志查询dto")
public class LogQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("日志类型: 0.用户日志 1.后台日志 2.登录日志")
    private Integer type = 0;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("日志标题")
    private String title;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("操作结果")
    private Integer status;

}
