package com.ruanchuang.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruanchuang.enums.BusinessStatus;
import com.ruanchuang.enums.BusinessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统操作日志表
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value = "SysLog对象", description = "系统操作日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("操作")
    private String title;

    @ApiModelProperty("操作类型;0.其他 1.新增 2.修改 3.删除")
    private BusinessType operType;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("请求URL")
    private String requestUrl;

    @ApiModelProperty("请求ip")
    private String requestIp;

    @ApiModelProperty("请求参数")
    private String requestParam;

    @ApiModelProperty("响应结果")
    private String responseResult;

    @ApiModelProperty("操作结果;0.正常 1.异常")
    private BusinessStatus status;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
