package com.sugon.iris.sugondomain.dtos.fileDtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MppSearchDto {
    /**
     * 案件名称
     */
    @ApiModelProperty(value="案件名称")
    private  String caseName;

    /**
     * 模板名称
     */
    @ApiModelProperty(value="模板名称")
    private  String templateName;

    /**
     *mpp表名
     */
    @ApiModelProperty(value="mpp表名")
    private  String tableName;

    /**
     *模板id
     */
    @ApiModelProperty(value="模板id")
    private Long fileTemlateId ;

    /**
     *查询开始
     */
    @ApiModelProperty(value="查询开始")
    private Integer perSize;

    /**
     *偏移量
     */
    @ApiModelProperty(value="偏移量")
    private Integer offset;

    /**
     * 勾选的清洗字段
     */
    @ApiModelProperty(value="勾选的清洗字段")
    List<String> checkFields;
}
