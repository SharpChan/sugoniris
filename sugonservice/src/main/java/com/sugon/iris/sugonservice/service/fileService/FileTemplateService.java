package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto4Txt;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileTemplateService {

     List<FileTemplateDto> getFileTemplateDtoList(User user,FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException;

    int fileTemplateInsert(User user,FileTemplateDto fileTemplateDto,List<Error> errorList) throws IllegalAccessException;

    int updateFileTemplate(User user,FileTemplateDto fileTemplateDto,List<Error> errorList) throws IllegalAccessException;

    int deleteFileTemplate(String[] idArr, List<Error> errorList);

    List<FileTemplateDto> getAllFileTemplateDtoAndDetailsList( List<Error> errorList) throws IllegalAccessException;

    FileTemplateDto4Txt getFileTemplateDtoAndDetailsListByTemplateId(Long templateId , List<Error> errorList) throws IllegalAccessException;

    void getTemplateTxt(HttpServletResponse response, Long templateId , List<Error> errorList) throws IllegalAccessException;

    boolean importTemplateTxt(MultipartFile file, User user, List<Error> errorList) throws IllegalAccessException;
}
