package com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos;

import com.sugon.iris.sugondomain.beans.sdmModelInfosBeans.ResultTableBean;
import lombok.Data;

import java.util.List;

@Data
public class ResultTableBeanDTO extends ResultTableBean {


    /**
     *结果表的列信息
     */
    private List<ResultColumnBeanDTO> resultColumns;
}
