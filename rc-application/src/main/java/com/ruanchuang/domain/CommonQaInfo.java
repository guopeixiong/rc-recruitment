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
 * @since 2023/11/30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("common_qa_info")
@ApiModel(value = "CommonQaInfo对象", description = "常见QA信息表")
public class CommonQaInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("问题")
    private String question ;

    @ApiModelProperty("回答")
    private String answer ;

    @ApiModelProperty("是否置顶,0.否 1.是")
    private Integer top ;

    @ApiModelProperty("是否启用,0.否 1.是")
    private Integer enable ;

    @ApiModelProperty("备注")
    private String remark ;

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
