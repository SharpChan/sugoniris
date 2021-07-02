package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import lombok.Data;

import java.util.List;

@Data
public class FileTemplateDto extends FileTemplateBean {

    /**
     * 模板的字段信息
     */
    private List<FileTemplateDetailDto> fileTemplateDetailDtoList;
}
