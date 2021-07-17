package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileCaseBean;
import lombok.Data;

import java.util.List;

@Data
public class FileCaseDto extends FileCaseBean {

    List<FileTableDto> fileTableDtoList;


}
