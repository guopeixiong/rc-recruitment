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
 * 用户信息表
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户信息表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("密码盐")
    private String salt;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱号")
    private String email;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户姓名")
    private String fullName;

    @ApiModelProperty("学号")
    private String stuNum;

    @ApiModelProperty("性别;0.女 1.男")
    private Integer sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户类型;0.普通用户 1.系统管理员")
    private Integer type;

    @ApiModelProperty("账号状态;0.正常 1.停用")
    private Integer status;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLogin;

    @ApiModelProperty("最后登录ip")
    private String loginIp;

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
