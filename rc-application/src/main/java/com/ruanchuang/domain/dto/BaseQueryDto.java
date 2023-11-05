package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2023/9/3
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("基础查询dto")
public class BaseQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty("页大小")
    @Max(value = 100, message = "页大小不能超过100")
    private Integer pageSize = 10;

    @ApiModelProperty("关键词")
    @Max(value = 50, message = "关键词不能超过50个字符")
    private String keyword;
}
