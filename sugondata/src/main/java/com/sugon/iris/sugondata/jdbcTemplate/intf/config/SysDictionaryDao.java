package com.sugon.iris.sugondata.jdbcTemplate.intf.config;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.SysDictionaryEntity;
import java.util.List;

public interface SysDictionaryDao {

    /**
     * 查询所有的字典项
     */
    List<SysDictionaryEntity> findSysDictionary(String dicGroup,List<Error> errorList);

    /**
     * 更新字典项
     */
    int updateSysDictionary(SysDictionaryEntity sysDictionaryEntity,List<Error> errorList);

    /**
     *保存字典项
     */
    int saveSysDictionary(SysDictionaryEntity sysDictionaryEntity,List<Error> errorList);

    int deleteSysDictionary(Long id, List<Error> errorList);

}
