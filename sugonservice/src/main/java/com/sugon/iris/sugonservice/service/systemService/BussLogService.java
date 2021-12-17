package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.BusinessLogDto;

import java.util.List;

public interface BussLogService {

    List<BusinessLogDto> queryLogs(List<Error> errorList);
}
