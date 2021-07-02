package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateGroupEntity;
import java.util.List;

public interface FileTemplateGroupMapper {

    List<FileTemplateGroupEntity> selectFileTemplateGroupList(FileTemplateGroupEntity fileTemplateGroupEntity);

    int fileTemplateGrouplInsert(List<FileTemplateGroupEntity> fileTemplateGroupEntityList);

    int deleteFileTemplateGroupById(String[] idArr);

}
