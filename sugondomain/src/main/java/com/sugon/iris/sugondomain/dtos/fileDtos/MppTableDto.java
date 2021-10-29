package com.sugon.iris.sugondomain.dtos.fileDtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class MppTableDto {


    @ApiModelProperty(value="字段列")
    List<FieldDto> cols;

    /**
     * 数据行   map【key：fieldName，value：数据库中的字段值】
     */
    @ApiModelProperty(value="数据行   map【key：fieldName，value：数据库中的字段值】")
    List<Map<String,Object>> rows;

    /**
     * 数据量
     */
    @ApiModelProperty(value="数据量")
    Integer count;
}


