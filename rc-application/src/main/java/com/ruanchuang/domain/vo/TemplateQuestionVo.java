package com.ruanchuang.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/7
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("报名表模板问题Vo")
public class TemplateQuestionVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("问题内容")
    private String content;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("选项")
    private List<String> options;

    @ApiModelProperty("是否必答")
    private String isRequire;

}
