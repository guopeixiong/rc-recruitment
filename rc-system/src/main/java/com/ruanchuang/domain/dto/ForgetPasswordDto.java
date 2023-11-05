package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2023/8/11
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("忘记密码参数")
public class ForgetPasswordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "邮箱不难为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码为6-20位")
    private String password;

    @NotNull(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码格式错误")
    private String code;

}
