package com.ruanchuang.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author guopeixiong
 * @Date 2024/1/17
 * @Email peixiongguo@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("邮件发送记录查询dto")
public class EmailSendRecordQueryDto extends BaseQueryDto {

    @ApiModelProperty("姓名关键字")
    private String name;

    @ApiModelProperty("邮箱关键字")
    private String email;

    @ApiModelProperty("标题关键字")
    private String title;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
