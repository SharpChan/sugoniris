package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import java.util.List;

public interface FileParsingFailedMapper {

    List<FileParsingFailedEntity> selectFileParsingFailedList(FileParsingFailedEntity fileParsingFailedEntity);

    int fileParsingFailedInsert(List<FileParsingFailedEntity> fileParsingFailedEntityList);

    int deleteFileParsingFailedById(String[] idArr);

    int deleteFileParsingFailedByMppid2errorid(Long  mppid2errorid);

    int deleteFileParsingFailedByFileDetailId(String[] fileDetailIdArr);

    int deleteFileParsingFailedByFileAttachmentId(Long fileAttachmentId);

}
