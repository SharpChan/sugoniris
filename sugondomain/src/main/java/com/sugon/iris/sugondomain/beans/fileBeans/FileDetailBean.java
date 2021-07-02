package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;

    /**
     *元素文件信息id
     */
    private Long fileAttachmentId;

    /**
     *文件类型
     */
    private String fileType;

    /**
     *文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 上传的数据行数
     */
    private Integer rowCount;

    /**
     * 模板id
     */
    private Long fileTemplateId;

    /**
     * 导入的数据数
     */
    private Integer importRowCount;

    /**
     * 案件编号
     */
    private String caseId;

    /**
     * 表名="base_"+模板配置前缀+"_"+md5加密案件编号
     */
    private String tableName;

    /**
     *用户编号
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 数据是否同步
     */
    private Boolean hasImport;
}
