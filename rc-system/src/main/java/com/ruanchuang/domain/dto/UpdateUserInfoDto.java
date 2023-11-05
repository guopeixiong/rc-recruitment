package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2023/8/8
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("修改用户个人信息参数")
public class UpdateUserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("昵称")
    @Length(min = 1, max = 20, message = "昵称长度为1到20位")
    private String nickName;

    @ApiModelProperty("用户姓名")
    @Length(min = 2, max = 20, message = "姓名长度为2到20位")
    private String fullName;

    @ApiModelProperty("学号")
    @Pattern(regexp = "^(202[0-9])(\\d){8}$", message = "学号格式错误")
    private String stuNum;

    @ApiModelProperty("手机号")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("性别")
    @Range(min = 0, max = 1, message = "性别值不合法")
    private Integer sex;

}
