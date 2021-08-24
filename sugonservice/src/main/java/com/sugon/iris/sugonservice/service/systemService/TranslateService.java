package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.TranslateDto;

import java.util.List;
import java.util.Map;

public interface TranslateService {

   Map<String,?> getTranslateList(String tsType, List<Error> errorList) throws IllegalAccessException;

   Integer addTranslate(String tsStr,Long menuId,String tsType,String translat, List<Error> errorList)throws IllegalAccessException;

   Integer updateTranslate(String tsStr,Long menuId,String tsType,String translat, List<Error> errorList) throws IllegalAccessException;

   int[] deleteTranslate(Long menuId,String tsType,List<Error> errorList);

}
