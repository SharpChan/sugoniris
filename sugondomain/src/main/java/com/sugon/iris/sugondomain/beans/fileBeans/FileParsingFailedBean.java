package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * 数据同步失败
 */

@Data
public class FileParsingFailedBean {

    /**
     *自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *文件详细详细表id
     */
    @ApiModelProperty(value="文件详细详细表id")
    private Long fileDetailId;

    /**
     *文件行号
     */
    @ApiModelProperty(value="文件行号")
    private String rowNumber;


    /**
     * 通过mpp的error_seq序列获取
     */
    @ApiModelProperty(value="通过mpp的error_seq序列获取")
    private Long mppId2ErrorId;

    /**
     *数据内容
     */
    @ApiModelProperty(value="数据内容")
    private String content;

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private Long caseId;

    /**
     * 表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     *用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
     * 创建时间呢
     */
    @ApiModelProperty(value="创建时间呢")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 模板id
     */
    @ApiModelProperty(value="模板id")
    private Long fileTemplateId;

    /**
     * 字段id
     */
    @ApiModelProperty(value="字段id")
    private Long fileTemplateDetailId;

    /**
     * 模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     * 字段关键字
     */
    @ApiModelProperty(value="字段关键字")
    private String fieldKey;

    /**
     *问题处理
     */
    @ApiModelProperty(value="问题处理")
    private Boolean mark;

    /**
     * 原始文件id
     */
    @ApiModelProperty(value="原始文件id")
    private Long fileAttachmentId;
}
