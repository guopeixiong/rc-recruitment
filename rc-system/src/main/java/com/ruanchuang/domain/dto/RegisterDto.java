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
 * @Date 2023/8/5
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("用户注册参数")
public class RegisterDto {

    @NotNull(message = "学号不能为空")
    @Pattern(regexp = "^(202[0-9])(\\d){8}$", message = "学号格式错误")
    @ApiModelProperty("学号")
    private String stuNum;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;

    @NotNull(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码格式错误")
    private String code;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

}
