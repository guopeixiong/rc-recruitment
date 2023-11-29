package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author guopx
 * @since 2023/11/28
 */
@Data
@Accessors(chain = true)
@ApiModel("用户修改表目标问题参数")
public class UpdateSignUpFormDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "问题id不能为空")
    @ApiModelProperty("问题id")
    private Long id;

    @NotNull(message = "问题答案不能为空")
    @Length(max = 1000, message = "答案长度不能超出1000字")
    @ApiModelProperty("问题答案")
    private String answer;

}
