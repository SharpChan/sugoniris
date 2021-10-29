package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTableBean;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeInfoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileTableDto extends FileTableBean {

    @ApiModelProperty(value="数据组")
    private FileDataGroupTableDto fileDataGroupTableDto;

    @ApiModelProperty(value="数据模板")
    private FileTemplateDto fileTemplateDto;

    //图谱节点
    @ApiModelProperty(value="图谱节点")
    private Neo4jNodeInfoDto neo4jNodeInfoDto;

    /**
     * 如果在FileDataGroupTable表存在记录，则已经勾选
     */
    @ApiModelProperty(value="如果在FileDataGroupTable表存在记录，则已经勾选")
    private boolean isChecked;

    @ApiModelProperty(value="案件名称")
    private String caseName;

    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     * 大小
     */
    @ApiModelProperty(value="大小")
    private String size;

    /**
     * 颜色
     */
    @ApiModelProperty(value="颜色")
    private String color;

    /**
     * 形状
     */
    @ApiModelProperty(value="形状")
    private String sharp;

    /**
     * 标签
     */
    @ApiModelProperty(value="标签")
    private String label;
}
