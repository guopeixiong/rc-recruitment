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
 * @Date 2024/1/14
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("回复咨询dto")
public class ReplyConsultingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("咨询id")
    @NotNull(message = "咨询id不能为空")
    private Long id;

    @ApiModelProperty("回复内容")
    @NotBlank(message = "回复内容不能为空")
    @Length(max = 1000, message = "回复内容长度不能超过1000")
    private String replyContent;

}
