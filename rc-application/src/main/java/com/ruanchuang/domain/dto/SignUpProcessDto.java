package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/8
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("报名流程Vo")
public class SignUpProcessDto implements Serializable {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空", groups = AddGroup.class)
    @Length(min = 1, max = 20, message = "名称长度在1到20之间", groups = {UpdateGroup.class, AddGroup.class})
    private String name;

    @ApiModelProperty("备注")
    @Length(min = 1, max = 100, message = "备注长度在1到100之间", groups = {UpdateGroup.class, AddGroup.class})
    private String remark;

    @ApiModelProperty("是否启用")
    @Range(min = 0, max = 1, message = "是否启用只能为0或1", groups = {UpdateGroup.class, AddGroup.class})
    private Integer enable;

    @ApiModelProperty("流程列表")
    @NotNull(message = "流程状态不能为空", groups = AddGroup.class)
    @Size(min = 2, max = 5, message = "流程状态个数必须在2到5个之间", groups = {AddGroup.class})
    private List<String> status;

    public interface AddGroup {}

    public interface UpdateGroup {}

}
