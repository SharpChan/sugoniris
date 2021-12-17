package com.sugon.iris.sugondomain.beans.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class BusinessLog {

    @ApiModelProperty(value="自增序列")
    private Long id;

    @ApiModelProperty(value="用户id")
    private Long userId;

    @ApiModelProperty(value="用户名")
    private String userName;

    @ApiModelProperty(value="业务名称")
    private String business;

    @ApiModelProperty(value="ip地址")
    private String ip;

    @ApiModelProperty(value="业务执行时间")
    private Timestamp accessTime;
}
