package com.sugon.iris.sugondomain.beans.fileBeans;

import lombok.Data;

@Data
public class FileRinseDetailBean{

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 清洗组id
     */
    private Long fileRinseGroupId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 备注
     */
    private String comment;

    /**
     * 创建者id
     */
    private Long userId;

}