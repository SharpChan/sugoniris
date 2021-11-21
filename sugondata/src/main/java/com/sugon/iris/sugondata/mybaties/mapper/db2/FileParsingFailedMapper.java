package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import java.util.List;
import java.util.Vector;

public interface FileParsingFailedMapper {

     List<FileParsingFailedEntity> selectFileParsingFailedList(FileParsingFailedEntity fileParsingFailedEntity);

    int fileParsingFailedInsert(Vector<FileParsingFailedEntity> fileParsingFailedEntityList);

    int deleteFileParsingFailedByMppid2errorid(Long  mppid2errorid);

    int deleteFileParsingFailedByFileAttachmentId(Long fileAttachmentId);

    int deleteFileParsingFailedByMppid2erroridBatch(List<Long> idArr);

    int countRecord(Long mppid2errorid);

    String selectMppTableName(Long mppid2errorid);

    int deleteFileParsingFailedByMppid2erroridAndFileField(Long  mppid2errorid,Long fileTemplateDetailId);

}
