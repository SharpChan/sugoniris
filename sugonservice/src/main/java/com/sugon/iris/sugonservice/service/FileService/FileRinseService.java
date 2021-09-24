package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;

import java.util.List;

public interface FileRinseService {

     Integer add(FileRinseGroupDto fileRinseDto, List<Error> errorList) throws IllegalAccessException;

     List<FileRinseGroupDto>   findFileRinseByUserId(Long userId, List<Error> errorList) throws IllegalAccessException;

     Integer modifyFileRinse(FileRinseGroupDto fileRinseDto, List<Error> errorList) throws IllegalAccessException;

     Integer deleteFileRinse(long id,List<Error> errorList) throws IllegalAccessException;
}
