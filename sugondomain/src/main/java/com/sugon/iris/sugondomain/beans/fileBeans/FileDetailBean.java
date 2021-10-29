package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * 压缩包内文件详细详细
 */
@Data
public class FileDetailBean {

    /**
     *自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *元素文件信息id
     */
    @ApiModelProperty(value="元素文件信息id")
    private Long fileAttachmentId;

    /**
     *文件类型
     */
    @ApiModelProperty(value="文件类型")
    private String fileType;

    /**
     *文件名称
     */
    @ApiModelProperty(value="文件名称")
    private String fileName;

    /**
     * 文件路径
     */
    @ApiModelProperty(value="文件路径")
    private String filePath;

    /**
     * 上传的数据行数
     */
    @ApiModelProperty(value="上传的数据行数")
    private Integer rowCount;

    /**
     * 模板id
     */
    @ApiModelProperty(value="模板id")
    private Long fileTemplateId;

    /**
     * 导入的数据数
     */
    @ApiModelProperty(value="导入的数据数")
    private Integer importRowCount;

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private Long caseId;

    /**
     * 表名="base_"+模板配置前缀+"_"+md5加密案件编号
     */
    @ApiModelProperty(value="表名=\"base_\"+模板配置前缀+\"_\"+md5加密案件编号")
    private String tableName;

    /**
     *用户编号
     */
    @ApiModelProperty(value="用户编号")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="表编号")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @ApiModelProperty(value="表编号")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 数据是否同步
     */
    @ApiModelProperty(value="表编号")
    private Boolean hasImport;

    /**
     * 表id
     */
    @ApiModelProperty(value="表编号")
    private Long fileTableId;

    /**
     * 错误信息
     */
    @ApiModelProperty(value="表编号")
    private String failureMessage;
}
