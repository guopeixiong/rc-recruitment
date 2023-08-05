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
 * <p>
 * 文件信息表
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_file")
@ApiModel(value = "SysFile对象", description = "文件信息表")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("原文件名")
    private String oldFileName;

    @ApiModelProperty("文件存储路径")
    private String path;

    @ApiModelProperty("文件备注")
    private String remark;

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
