package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileTemplateDetailBean {
    /**
     *自增序列
     */
    private Long id;

    /**
     *模板id
     */
    private Long templateId;

    /**
     *字段名称
     */
    private String fieldName;

    /**
     *字段关键字&&进行分割
     */
    private String fieldKey;

    /**
     *正则表达式&&进行分割
     */
    private String regular;

    /**
     *排序字段
     */
    private String sortNo;

    /**
     *备注
     */
    private String comment;

    /**
     *用户id
     */
    private Long userId;

    /**
     * 关键字排除
     */
    private String exclude;

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

}
