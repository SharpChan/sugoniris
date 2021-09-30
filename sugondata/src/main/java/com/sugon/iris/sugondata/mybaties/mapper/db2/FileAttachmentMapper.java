package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileAttachmentEntity;
import java.util.List;

public interface FileAttachmentMapper {

    List<FileAttachmentEntity> selectFileAttachmentList(FileAttachmentEntity fleAttachmentEntity);

    int batchFileAttachmentInsert(List<FileAttachmentEntity> fileAttachmentEntityList);

    int updateFileAttachment(FileAttachmentEntity fileAttachmentEntity);

    int updateFileAttachmentTemplateGroup (FileAttachmentEntity fileAttachmentEntity);

    int deleteFileAttachmentById(FileAttachmentEntity fileAttachmentEntity);

    List<String>  getFileAttachmentIds(String caseId);

    FileAttachmentEntity selectFileAttachmentByPrimaryKey(Long id);
}
