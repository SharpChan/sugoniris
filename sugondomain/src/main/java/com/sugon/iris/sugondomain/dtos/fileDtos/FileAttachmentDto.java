package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileAttachmentBean;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileAttachmentDto extends FileAttachmentBean {

    /**
     * 模板信息（一个文件夹对应一个模板组）
     */
   private FileTemplateGroupDto fileTemplateGroupDto;

    /**
     * 导入文件列表
     */
   private List<FileDetailDto>  fileDetailDtoList;

   public List<FileDetailDto> getFileDetailDtoList(){
       if(CollectionUtils.isEmpty(fileDetailDtoList)){
           return new ArrayList<FileDetailDto>();
       }
       return fileDetailDtoList;
   }
}
