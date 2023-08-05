package com.ruanchuang.mapper.domain;

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
 * 报名表回答表
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sign_up_from_answer")
@ApiModel(value = "SignUpFromAnswer对象", description = "报名表回答表")
public class SignUpFromAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("问题id")
    private Long questionId;

    @ApiModelProperty("文本答案")
    private String textAnswer;

    @ApiModelProperty("选项答案;保存选项id, 多个选项用 , 分割")
    private String optionsAnswer;

    @ApiModelProperty("答案类型;0.文本答案 1.选项答案")
    private Integer type;

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
