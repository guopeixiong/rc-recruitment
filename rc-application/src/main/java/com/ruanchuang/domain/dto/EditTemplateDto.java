package com.ruanchuang.domain.dto;

import com.ruanchuang.domain.vo.TemplateQuestionVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/7
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("修改报名表dto")
public class EditTemplateDto {

    @ApiModelProperty("报名表id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("报名表名称")
    private String name;

    @ApiModelProperty("流程id")
    private Long processId;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("题目列表")
    private List<TemplateQuestionVo> questions;

}
