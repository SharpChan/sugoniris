package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppSearchDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppTableDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FileDataMergeService {

    List<FileCaseDto> getCases(Long userId,List<Error> errorList) throws IllegalAccessException;

    MppTableDto getTableRecord(MppSearchDto mppSearchDto,List<Error> errorList);

    Integer getTableRecordQuantity(MppSearchDto mppSearchDto,List<Error> errorList);

    void doUserDefinedRinse(Long caseId,Long userId,List<Error> errorList);

    void getCsv(MppSearchDto mppSearchDto, HttpServletResponse response) throws Exception;

    void mergeExport(Long caseId, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException;

    void mergeExportAsync(Long caseId, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException;

    void mergeExportAsyncForSheet(Long caseId, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException;
}
