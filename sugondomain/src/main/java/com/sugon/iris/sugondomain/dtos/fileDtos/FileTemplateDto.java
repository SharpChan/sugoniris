package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileTemplateDto extends FileTemplateBean {

    /**
     * 模板的字段信息
     */
    private List<FileTemplateDetailDto> fileTemplateDetailDtoList;



    public List<FileTemplateDetailDto> getFileTemplateDetailDtoList(){
        if(CollectionUtils.isEmpty(fileTemplateDetailDtoList)){
            this.fileTemplateDetailDtoList = new ArrayList<FileTemplateDetailDto>();
        }
        return this.fileTemplateDetailDtoList;
    }
}
