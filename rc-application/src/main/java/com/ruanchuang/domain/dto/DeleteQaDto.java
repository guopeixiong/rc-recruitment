package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2024/1/2
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("删除常见问题")
public class DeleteQaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ids")
    @Size(min = 1, max = 10, message = "一次只能删除1到10条数据")
    private List<Long> ids;

}
