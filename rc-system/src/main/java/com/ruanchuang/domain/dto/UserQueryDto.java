package com.ruanchuang.domain.dto;

import com.ruanchuang.model.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author guopeixiong
 * @Date 2023/12/24
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("用户查询参数")
public class UserQueryDto extends PageDto {

    @ApiModelProperty("学号")
    private String stuNum;

    @ApiModelProperty("姓名")
    private String fullName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

}
