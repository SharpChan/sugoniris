package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileImportCountService {

     List<FileCaseDto> getImportCount(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException;

     List<FileParsingFailedDto> getFileParsingFailed(Long userId,Long fileDetailId,List<Error> errorList) throws IllegalAccessException;

     Boolean downloadErrorsExcelZip(HttpServletResponse response, HttpServletRequest request,Long fileDetailId);

     void downloadErrorsExcel(HttpServletResponse response, HttpServletRequest request,Long fileDetailId) throws IOException;

     Integer dataAmendment(List<MultipartFile> files, Long  fileDetailId,List<Error> errorList) throws IOException;

}
