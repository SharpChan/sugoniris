package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDto;

import java.util.List;

public interface FileRinseService {

     Integer add(FileRinseDto fileRinseDto,List<Error> errorList) throws IllegalAccessException;

     List<FileRinseDto>   findFileRinseByUserId(Long userId,List<Error> errorList) throws IllegalAccessException;

     Integer modifyFileRinse(FileRinseDto fileRinseDto,List<Error> errorList) throws IllegalAccessException;

     Integer deleteFileRinse(long id,List<Error> errorList) throws IllegalAccessException;
}
