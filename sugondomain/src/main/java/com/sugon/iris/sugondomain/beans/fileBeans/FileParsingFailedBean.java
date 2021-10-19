package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;

    /**
     *文件详细详细表id
     */
    private Long fileDetailId;

    /**
     *文件行号
     */
    private String rowNumber;


    /**
     * 通过mpp的error_seq序列获取
     */
    private Long mppId2ErrorId;

    /**
     *数据内容
     */
    private String content;

    /**
     * 案件编号
     */
    private Long caseId;

    /**
     * 表名
     */
    private String tableName;

    /**
     *用户id
     */
    private Long userId;

    /**
     * 创建时间呢
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 模板id
     */
    private Long fileTemplateId;

    /**
     * 字段id
     */
    private Long fileTemplateDetailId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 字段关键字
     */
    private String fieldKey;

    /**
     *问题处理
     */
    private Boolean mark;
}
