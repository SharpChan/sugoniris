package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseRegularDto;

import java.util.List;

public interface FileRinseRegularService {

    Integer add(FileRinseRegularDto fileRinseRegularDto,List<Error> errorList) throws IllegalAccessException;

    List<FileRinseRegularDto> findFileRinseDetailByFileRinseDetailId(Long fileRinseDetailId, List<Error> errorList) throws IllegalAccessException;

    Integer deleteByPrimaryKey(Long id,List<Error> errorList) throws IllegalAccessException;

    Integer deleteByFileRinseDetailId(Long fileRinseDetailId, List<Error> errorList) throws IllegalAccessException;
}
