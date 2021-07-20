package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileCaseBean;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileCaseDto extends FileCaseBean {

    List<FileTableDto> fileTableDtoList;

    /**
     * 导入压缩包或文件详细信息
     */
    List<FileAttachmentDto>  fileAttachmentDtoList;

    public List<FileAttachmentDto> getFileAttachmentDtoList(){
        if(CollectionUtils.isEmpty(fileAttachmentDtoList)){
            return new ArrayList<FileAttachmentDto>();
        }
        return fileAttachmentDtoList;
    }
}
