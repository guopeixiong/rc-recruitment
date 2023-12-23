package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author guopeixiong
 * @Date 2023/12/23
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("用户状态参数")
public class UserStatusDto {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "用户状态不能为空")
    private Integer status;

}
