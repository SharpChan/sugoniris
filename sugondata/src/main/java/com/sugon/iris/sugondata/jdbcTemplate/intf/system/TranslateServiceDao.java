package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.TranslateEntity;

import java.util.List;

public interface TranslateServiceDao {

   List<TranslateEntity> getZhCnTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList);

   int saveTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList);

   Integer updateTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList);

   int[] deleteTranslate(List<Object[]> idList, List<Error> errorList);
}
