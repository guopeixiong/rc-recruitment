package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @Author guopeixiong
 * @Date 2023/8/12
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("修改密码参数")
public class UpdatePwdDto {

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;

    @NotNull(message = "旧密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度在6-20位置")
    @ApiModelProperty("旧密码")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度在6-20位置")
    @ApiModelProperty("新密码")
    private String newPassword;

    @NotNull(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码格式错误")
    @ApiModelProperty("验证码")
    private String code;

}
