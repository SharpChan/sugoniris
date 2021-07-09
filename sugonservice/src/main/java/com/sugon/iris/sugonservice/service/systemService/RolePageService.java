package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.RolePageDto;
import java.util.List;

public interface RolePageService {

      Integer[] saveRolePage(List<RolePageDto> rolePageDtoList,List<Error> errorList);

      Integer[] deleteRolePage(List<Long> idList,List<Error> errorList);
}
