package com.sugon.iris.sugondomain.beans.fileBeans;

import lombok.Data;

@Data
public class FileDataGroupTableBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 数据组id
     */
    private Long dataGroupId;

    /**
     * 表编号
     */
    private Long tableId;

    /**
     *创建者id
     */
    private Long createUserId;
}
