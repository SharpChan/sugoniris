package com.sugon.iris.sugondata.jdbcTemplate.intf.system;



import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.securityModuleEntities.WhiteIpEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface NginxServiceDao {

    List<WhiteIpEntity> queryWhiteIp(WhiteIpEntity whiteIpEntity, List<Error> errorList);

    int saveWhiteIp(WhiteIpEntity whiteIpEntity, List<Error> errorList);

    int deleteWhiteIp(Long id, List<Error> errorList);

}
