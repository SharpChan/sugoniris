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
     * 已导入的数据量
     */
    Integer  caseImportRowCount=0;

    /**
     * 该案件数据总量
     */
    Integer caseRowCount=0;

    /**
     * 导入压缩包或文件详细信息
     */
    List<FileAttachmentDto>  fileAttachmentDtoList;

    public List<FileAttachmentDto> getFileAttachmentDtoList(){
        if(CollectionUtils.isEmpty(fileAttachmentDtoList)){
              this.fileAttachmentDtoList = new ArrayList<>();
        }
        return this.fileAttachmentDtoList;
    }

    public void  rowCount(){
        if(CollectionUtils.isEmpty(fileAttachmentDtoList)){
            return;
        }
        for(FileAttachmentDto fileAttachmentDto : fileAttachmentDtoList){
            fileAttachmentDto.setRowCount();
            caseImportRowCount += fileAttachmentDto.getAttachmentImportRowCount();
            caseRowCount +=  fileAttachmentDto.getAttachmentImportRowCount();
        }
    }
}
