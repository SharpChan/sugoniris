package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateDetailBean;
import lombok.Data;

@Data
public class FileTemplateDetailDto extends FileTemplateDetailBean {

    /**
     *清洗字段类型名称
     */
    private String fileRinseDetailTypeName;

}
