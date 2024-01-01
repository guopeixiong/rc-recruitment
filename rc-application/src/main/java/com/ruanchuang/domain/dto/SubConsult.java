package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author guopx
 * @since 2023/12/18
 */
@Data
@Accessors(chain = true)
@ApiModel("提交咨询信息")
public class SubConsult implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "咨询信息不能为空")
    @Length(max = 1000, message = "咨询信息长度不能超出1000字")
    private String content;

}
