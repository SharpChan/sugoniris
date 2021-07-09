package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.RolePageDto;
import com.sugon.iris.sugonservice.service.systemService.RolePageService;
import java.util.List;

public class RolePageServiceImpl implements RolePageService {
    @Override
    public Integer[] saveRolePage(List<RolePageDto> rolePageDtoList, List<Error> errorList) {
        return new Integer[0];
    }

    @Override
    public Integer[] deleteRolePage(List<Long> idList, List<Error> errorList) {
        return new Integer[0];
    }
}
