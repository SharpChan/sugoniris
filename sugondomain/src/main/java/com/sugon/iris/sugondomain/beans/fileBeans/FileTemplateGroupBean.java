package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class FileTemplateGroupBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 模板组ID
     */
    private Long groupId;

    /**
     * 模板组名称
     */
    private String groupName;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 备注
     */
    private String comment;

    /**
     * 创建时间呢
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     *创建者
     */
    private Long userId;



}
