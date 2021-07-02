package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 模板信息
 */

@Data
public class FileTemplateBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     *模板名称
     */
    private String templateName;

    /**
     *对应的表名关键字
     */
    private String tablePrefix;

    /**
     *自动匹配的关键字以&&分隔
     */
    private String templateKey;

    /**
     *备注
     */
    private String comment;

    /**
     *创建者
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
