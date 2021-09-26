package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import java.util.List;

public interface FileTemplateDetailMapper {
    List<FileTemplateDetailEntity> selectFileTemplateDetailList(FileTemplateDetailEntity fileTemplateDetailEntity);

    int fileTemplateDetailInsert(FileTemplateDetailEntity fileTemplateDetailEntity);

    int updateFileTemplateDetail(FileTemplateDetailEntity fileTemplateDetailEntity);

    int updateByTemplateIdSelective(Long templateId);

    int deleteFileTemplateDetailById(String[] idArr);

    int deleteFileTemplateDetailByTemplatId(String[] templatIdArr);
}
