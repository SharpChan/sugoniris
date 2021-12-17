package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.BusinessLogEntity;

import java.util.List;

public interface SysBusinessLogMapper {

   int saveBusinessLog(BusinessLogEntity businessLogEntity);

   List<BusinessLogEntity> getAllBusinessLogs();


}
