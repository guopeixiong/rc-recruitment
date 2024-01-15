package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/2
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("id列表入参")
public class IdsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ids")
    private List<Long> ids;

}
