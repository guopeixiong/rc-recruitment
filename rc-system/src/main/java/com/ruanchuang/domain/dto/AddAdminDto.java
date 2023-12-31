package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author guopeixiong
 * @Date 2023/12/31
 * @Email peixiongguo@163.com
 */

@Data
@Accessors(chain = true)
@ApiModel("新增管理员参数")
public class AddAdminDto {

    @ApiModelProperty("学号")
    @NotNull(message = "学号不能为空")
    @Pattern(regexp = "^(202[0-9])(\\d){8}$", message = "学号格式错误")
    private String stuNum;

    @ApiModelProperty("邮箱")
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("姓名")
    @NotNull(message = "姓名不能为空")
    @Length(min = 1, max = 10, message = "姓名长度必须在1到10个字之间")
    private String fullName;

}
