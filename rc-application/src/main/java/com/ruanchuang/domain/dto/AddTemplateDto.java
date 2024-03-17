package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/1/6
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("新增报名表模板dto")
public class AddTemplateDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报名表名称")
    @NotBlank(message = "报名表名称不能为空")
    @Length(min = 1, max = 20, message = "报名表名称长度不能超过20")
    private String name;

    @ApiModelProperty("流程id")
    @NotNull(message = "流程不能为空")
    private Long processId;

    @ApiModelProperty("报名表类型")
    private Integer type = 0;

}
