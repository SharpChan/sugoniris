package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class FileAttachmentBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 文件名称
     */
    @ApiModelProperty(value="文件名称")
    private String fileName;

    /**
     * 文件类型
     */
    @ApiModelProperty(value="文件类型")
    private String fileType;

    /**
     * 文件大小
     */
    @ApiModelProperty(value="文件大小")
    private String fileSize;

    /**
     *文件存放路径
     */
    @ApiModelProperty(value="文件存放路径")
    private String attachment;

    /**
     *有没有解压
     */
    @ApiModelProperty(value="有没有解压")
    private Boolean hasDecompress;

    /**
     * 创建时间呢
     */
    @ApiModelProperty(value="创建时间呢")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *上传者id
     */
    @ApiModelProperty(value="上传者id")
    private long userId;

    /**
     *是否已经上传到mpp
     */
    @ApiModelProperty(value="是否已经上传到mpp")
    private Boolean hasImport;

    /**
     *案件内部编号
     */
    @ApiModelProperty(value="案件内部编号")
    private Long caseId;

    /**
     *模板组编号
     */
    @ApiModelProperty(value="模板组编号")
    private Long templateGroupId;

    /**
     *模板组名称
     */
    @ApiModelProperty(value="模板组名称")
    private String templateGroupName;
}
