package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppSearchDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppTableDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface FileDataMergeService {

    List<FileCaseDto> getCases(Long userId,List<Error> errorList) throws IllegalAccessException;

    MppTableDto getTableRecord(MppSearchDto mppSearchDto,List<Error> errorList);

    Integer getTableRecordQuantity(MppSearchDto mppSearchDto,List<Error> errorList);

    void getCsv(MppSearchDto mppSearchDto, HttpServletResponse response) throws Exception;

    void mergeExport(Long caseId, HttpServletResponse response) throws IOException;
}
