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
 * @Date 2023/8/4
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("登录参数")
public class LoginDto {

    @NotNull(groups = LoginByPhoneOrStuNum.class, message = "学号不能为空")
    @Pattern(groups = LoginByPhoneOrStuNum.class, regexp = "^(202[0-9])(\\d){8}$", message = "学号格式错误")
    @ApiModelProperty("学号")
    private String stuNum;

    @NotNull(groups = LoginByPhoneOrStuNum.class, message = "密码不能为空")
    @Length(min = 6, max = 20, groups = LoginByPhoneOrStuNum.class, message = "密码格式错误")
    @ApiModelProperty("密码")
    private String password;

    @Email(groups = LoginByEmail.class, message = "邮箱格式错误")
    @NotNull(groups = LoginByEmail.class, message = "邮箱不能为空")
    @ApiModelProperty("邮箱")
    private String email;

    @NotNull(groups = LoginByEmail.class, message = "验证码不能为空")
    @Length(min = 6, max = 6, groups = LoginByEmail.class, message = "验证码格式错误")
    @ApiModelProperty("邮箱验证码")
    private String code;

    /**
     * 手机号或学号登录方式
     */
    public interface LoginByPhoneOrStuNum {}

    /**
     * 邮箱验证码登录方式
     */
    public interface LoginByEmail{}

}
