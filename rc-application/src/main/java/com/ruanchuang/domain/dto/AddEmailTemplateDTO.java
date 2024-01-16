package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/1/16
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("新增邮件模板dto")
public class AddEmailTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邮件标题")
    @NotBlank(message = "邮件标题不能为空")
    @Length(max = 20, message = "邮件标题长度不能超过20")
    private String title;

    @ApiModelProperty("邮件内容")
    @NotBlank(message = "邮件内容不能为空")
    @Length(max = 1000, message = "邮件标题长度不能超过1000")
    private String content;

}
