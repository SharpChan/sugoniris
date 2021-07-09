package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import java.util.List;

public interface FileDetailMapper {

    List<FileDetailEntity> selectFileDetailList(FileDetailEntity fileDetailEntity);

    int fileDetailInsert(FileDetailEntity fileDetailEntity);

    int deleteFileDetailById(String[] idArr);

    int deleteFileDetailByFileAttachmentId(Long fileAttachmentId);

    String selectTableName(FileDetailEntity fileDetailEntity);

}
