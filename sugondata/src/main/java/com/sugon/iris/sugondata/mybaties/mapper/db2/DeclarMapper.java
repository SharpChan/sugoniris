package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarInfoBeanEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarationDetailEntity;
import java.util.List;

public interface DeclarMapper{

  List<DeclarInfoBeanEntity> getDeclarInfoByUserId(Long userId);

  int saveDeclarationDetail(List<DeclarationDetailEntity>  declarationDetailEntityList);

  List<DeclarationDetailEntity>  findDeclarationDetail(DeclarationDetailEntity declarationDetailEntity);
}
