package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/1/1
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("新增常见问题dto")
public class AddQaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题")
    @Length(min = 1, max = 255, message = "问题长度必须在1-255之间")
    private String question;

    @ApiModelProperty("回答")
    @Length(min = 1, max = 255, message = "回答长度必须在1-255之间")
    private String answer;

    @ApiModelProperty("备注")
    @Length(min = 1, max = 255, message = "备注长度必须在1-255之间")
    private String remark;

    @ApiModelProperty("是否启用")
    @Range(min = 0, max = 1, message = "非法输入")
    private Integer enable = 0;

    @ApiModelProperty("是否置顶")
    @Range(min = 0, max = 1, message = "非法输入")
    private Integer top = 0;

}
