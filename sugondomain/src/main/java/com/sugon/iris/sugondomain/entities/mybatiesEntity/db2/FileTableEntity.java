package com.sugon.iris.sugondomain.entities.mybatiesEntity.db2;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTableBean;
import lombok.Data;

@Data
public class FileTableEntity extends FileTableBean {

    /**
     * 案件名称
     */
    private String caseName;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 标签
     */
    private String label;
}
