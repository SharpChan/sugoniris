package com.sugon.iris.sugonservice.service.searchService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.searchDtos.TableRecordSearchDto;
import java.util.List;

public interface TableRecordSearchService {

    List<TableRecordSearchDto> getRecordsByUserId(Long userId, String condition,List<Error> errorList) throws IllegalAccessException;
}
