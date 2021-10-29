package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FileCaseBean {

    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private String caseNo;

    /**
     * 案件名称
     */
    @ApiModelProperty(value="案件名称")
    private String caseName;

    /**
     * 案件信息
     */
    @ApiModelProperty(value="案件信息")
    private String caseInfo;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
     * 清洗地址，http或者websocket
     */
    @ApiModelProperty(value="清洗地址，http或者websocket")
    private String rinseUrl;

    /**
     * 清洗次数
     */
    @ApiModelProperty(value="清洗次数")
    private Integer rinseTimes;


    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

}
