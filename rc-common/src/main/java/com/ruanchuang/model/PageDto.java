package com.ruanchuang.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 通用分页dto
 * @Author guopeixiong
 * @Date 2023/7/30
 * @Email peixiongguo@163.com
 */
@Data
@ApiModel("通用分页查询参数")
public class PageDto {

    @ApiModelProperty("页码")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;

    @ApiModelProperty("页大小")
    @Max(value = 200, message = "每页数据不能大于200条")
    private Integer pageSize = 10;

}
