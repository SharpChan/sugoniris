package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import java.util.List;

public interface FileRinseDetailService {

     Long add(FileRinseDetailDto fileRinseDetailDto, List<Error> errorList) throws IllegalAccessException;

     List<FileRinseDetailDto>   findFileRinseDetailByGroupId(Long groupId, List<Error> errorList) throws IllegalAccessException;

     Integer modifyFileRinse(FileRinseDetailDto fileRinseDetailDto, List<Error> errorList) throws IllegalAccessException;

     Integer deleteFileRinse(Long id, List<Error> errorList) throws IllegalAccessException;
}
