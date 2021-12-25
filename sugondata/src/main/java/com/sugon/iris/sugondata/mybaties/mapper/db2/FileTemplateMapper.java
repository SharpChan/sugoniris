package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import java.util.List;

public interface FileTemplateMapper {
    List<FileTemplateEntity> selectFileTemplateList(FileTemplateEntity fileTemplateEntity);

    List<FileTemplateEntity>  selectFileTemplateForJingZhen();

    int fileTemplateInsert(FileTemplateEntity fileTemplateEntity);

    int updateFileTemplate(FileTemplateEntity fileTemplateEntity);

    int deleteFileTemplateById(String[] idArr);

    FileTemplateEntity selectFileTemplateByPrimaryKey(Long id);
}
