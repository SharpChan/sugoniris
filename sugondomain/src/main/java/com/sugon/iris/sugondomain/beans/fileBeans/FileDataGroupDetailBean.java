package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileDataGroupDetailBean {

    /**
     * 自增序列
     */
    private Long id;
    /**
     * 数据组内用户id
     */
    private Long userId;

    /**
     *数据组id
     */
    private Long fileDataGroupId;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     * 创建者id
     */
    private Long createUserId;
}
