package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessRepeatDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.MppErrorInfoEntity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface FileDoParsingService {

  void  doParsingCsv(Long userId, Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, String insertSql,
                     Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId, Set<Long> ipSet, Set<Long> phoneSet, List<Error> errorList) throws IOException;

  void  doParsingExcel(Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos,
                       String insertSql,Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId,List<Error> errorList) throws IOException, InvalidFormatException;

  void doRinse(FileTemplateDto fileTemplateDto,Object[] tableInfos, Long fileSeq, List<Error> errorList) throws IllegalAccessException;

  void doRepeat( String tableName, List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList, String repeatSql) throws InterruptedException, ExecutionException;

  void dealWithfailed(List<FileParsingFailedEntity> fileParsingFailedEntityListSql, StringBuffer errorBuffer) throws IOException, SQLException;

}
