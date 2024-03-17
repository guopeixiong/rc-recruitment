package com.ruanchuang.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author guopeixiong
 * @Date 2024/1/5
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("报名表模板信息Vo")
public class SignUpFormVo implements Serializable {

    private static final long SerialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("流程id")
    private Long processId;

    @ApiModelProperty("流程名称")
    private String processName;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
