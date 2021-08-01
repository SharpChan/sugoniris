package com.sugon.iris.sugonservice.service.declarService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarInfoDto;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import java.util.List;

public interface DeclarService {

   List<DeclarInfoDto> getDeclarInfo(Long userId, List<Error> errorList) throws IllegalAccessException;

   List<DeclarationDetailDto> getDeclarDetail(Long userId,String status, List<Error> errorList) throws IllegalAccessException;

   int  saveDeclaration(User user, List<DeclarationDetailDto> declarationDetailDtoList, List<Error> errorList)throws IllegalAccessException;
}
