package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileAttachmentBean;
import lombok.Data;

@Data
public class FileAttachmentDto extends FileAttachmentBean {

    /**
     * 模板信息（一个文件夹对应一个模板组）
     */
   private FileTemplateGroupDto fileTemplateGroupDto;
}
