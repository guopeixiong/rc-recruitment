package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2023/8/27
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("提交表单参数")
public class SubmitFormDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "模板id不能为空")
    @Pattern(regexp = "\\d{1,20}", message = "非法提交内容")
    @ApiModelProperty("模板id")
    private Long templateId;

    @NotNull(message = "问题id不能为空")
    @Pattern(regexp = "\\d{1,20}", message = "非法提交内容")
    @ApiModelProperty("问题id")
    private Long id;

    @NotNull(message = "问题答案不能为空")
    private String answer;

    @ApiModelProperty("是否必答")
    private Integer isRequire;

    @NotNull(message = "问题类型不能为空")
    @ApiModelProperty("问题类型")
    private Integer type;

}
