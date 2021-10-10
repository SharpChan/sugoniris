package com.sugon.iris.sugondomain.dtos.fileDtos;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class MppTableDto {



    List<FieldDto> cols;

    /**
     * 数据行   map【key：fieldName，value：数据库中的字段值】
     */
    List<Map<String,Object>> rows;

    /**
     * 数据量
     */
    Integer count;
}


