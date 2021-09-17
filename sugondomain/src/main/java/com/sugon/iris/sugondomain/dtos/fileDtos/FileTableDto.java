package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTableBean;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeInfoDto;
import lombok.Data;

@Data
public class FileTableDto extends FileTableBean {

    private FileDataGroupTableDto fileDataGroupTableDto;

    private FileTemplateDto fileTemplateDto;

    //图谱节点
    private Neo4jNodeInfoDto neo4jNodeInfoDto;

    /**
     * 如果在FileDataGroupTable表存在记录，则已经勾选
     */
    private boolean isChecked;

    private String caseName;

    private String templateName;

    /**
     * 大小
     */
    private String size;

    /**
     * 颜色
     */
    private String color;

    /**
     * 形状
     */
    private String sharp;

    /**
     * 标签
     */
    private String label;
}
