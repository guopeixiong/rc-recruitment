package com.ruanchuang.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author guopx
 * @since 2023/12/4
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sign_up_process_status")
@ApiModel(value = "SignUpProcessStatus对象", description = "报名流程状态表")
public class SignUpProcessStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("流程状态名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("下一状态id")
    private Long nextStatusId;

    @ApiModelProperty("流程id")
    private Long processId;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除;0.否 1.是")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty("版本号")
    @Version
    private Integer version;

}
