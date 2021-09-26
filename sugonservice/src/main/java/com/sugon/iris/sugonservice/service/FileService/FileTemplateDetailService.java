package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import java.util.List;

public interface FileTemplateDetailService {

     List<FileTemplateDetailDto> getFileTemplateDetailDtoList(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException;

    int fileTemplateDetailInsert(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException;

    int updateFileTemplateDetail(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException;

    int removeBoundByTemplateId(Long templateId,List<Error> errorList);

    int deleteFileTemplateDetail(String[] idArr, List<Error> errorList);
}
