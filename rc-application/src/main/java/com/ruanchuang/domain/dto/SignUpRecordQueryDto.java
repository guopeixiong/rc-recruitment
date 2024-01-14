package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/1/14
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("报名表查询dto")
public class SignUpRecordQueryDto implements Serializable {

    private static long serialVersionUID = 1L;

    private Integer pageNum = 1;

    @Max(value = 50, message = "一次最多只能查询50条数据")
    private Integer pageSize = 20;

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("流程状态id")
    private Long statusId;

}
