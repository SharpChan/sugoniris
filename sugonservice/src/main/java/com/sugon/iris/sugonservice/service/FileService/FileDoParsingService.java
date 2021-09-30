package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileDoParsingService {

  void  doParsingCsv(Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, String sqlInsert, Map<Long,List<String>> regularMap, List<Error> errorList) throws IOException;

  void  doParsingExcel(Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, List<Error> errorList);

}
