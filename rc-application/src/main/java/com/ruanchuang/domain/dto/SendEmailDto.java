package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/15
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("发送邮件通知dto")
public class SendEmailDto {

    @ApiModelProperty("标题")
    @NotBlank(message = "邮件标题不能为空")
    @Length(max = 20, message = "标题长度不能超过20个字")
    private String title;

    @ApiModelProperty("内容")
    @NotBlank(message = "邮件内容不能为空")
    @Length(max = 1000, message = "邮件内容长度不能超过1000个字")
    private String content;

    @ApiModelProperty("接收人id")
    @Size(min = 1, max = 100, message = "接收人不能超过100个")
    private List<Long> targetIds;

    @ApiModelProperty("是否保存为模板")
    private Integer saveAsTemplate = 0;

}
