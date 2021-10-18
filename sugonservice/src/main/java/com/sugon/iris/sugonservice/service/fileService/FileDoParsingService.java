package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileDoParsingService {

  void  doParsingCsv(Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, String insertSql,
                     Map<Long, FileRinseDetailDto>  regularMap,Long fileSeq, Long fileAttachmentId, List<Error> errorList) throws IOException;

  void  doParsingExcel(Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos,
                       String insertSql,Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId,List<Error> errorList) throws IOException, InvalidFormatException;

  void doRinse(FileTemplateDto fileTemplateDto,Object[] tableInfos, Long fileSeq, List<Error> errorList) throws IllegalAccessException;

}
