package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author guopeixiong
 * @Date 2024/3/17
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("轮播图绑定活动dto")
public class ImageBindActivityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片id")
    @NotNull(message = "图片id不能为空")
    private Long id;

    @ApiModelProperty("活动id")
    private Long activityId;

}
