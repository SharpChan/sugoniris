package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileDataGroupBeanBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 数据组名称
     */
    private String groupName;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 创建者id
     */
    private Long userId;
}
