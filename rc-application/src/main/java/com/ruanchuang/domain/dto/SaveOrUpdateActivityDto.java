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
 * @Date 2024/3/17
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("新增或修改活动信息dto")
public class SaveOrUpdateActivityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @NotNull(groups = UpdateActivity.class, message = "不能为空")
    private Long id;

    @ApiModelProperty("活动名称")
    @NotBlank(groups = AddActivity.class, message = "活动名称不能为空")
    @Length(groups = UpdateActivity.class, min = 1, max = 20, message = "长度在1-20之间")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("报名表id")
    private Long templateId;

    @ApiModelProperty("活动内容")
    private String content;

    public interface AddActivity {}

    public interface UpdateActivity {}

}
