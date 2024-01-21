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
 * @Author guopeixiong
 * @Date 2024/1/21
 * @Email peixiongguo@163.com
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("index_intro_info")
@ApiModel(value = "IndexIntroInfo对象", description = "首页简介信息表")
public class IndexIntroInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用 0.否 1.是")
    private Integer enable;

    @ApiModelProperty("内容")
    private String content;

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
