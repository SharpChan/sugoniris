package com.sugon.iris.sugondomain.beans.fileBeans;

import lombok.Data;

@Data
public class FileRinseGroupBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 清洗字段类型组名称
     */
    private String fileRinseName;

    /**
     * 备注
     */
    private String comment;

    /**
     * websocket地址
     */
    private String websocketUrl;

    /**
     * 创建者id
     */
    private Long userId;

}