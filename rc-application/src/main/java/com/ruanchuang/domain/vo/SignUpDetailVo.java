package com.ruanchuang.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2023/11/5
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("报名表详细信息Vo")
public class SignUpDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报名表id")
    private Long id;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("问题id")
    private Long questionId;

    @ApiModelProperty("问题内容")
    private String question;

    @ApiModelProperty("答案id")
    private Long answerId;

    @ApiModelProperty("答案内容")
    private String answer;

    @ApiModelProperty("选择题答案")
    private String optAnswer;

}
