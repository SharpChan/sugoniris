package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseRegularMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseRegularDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseRegularEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileRinseRegularService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileRinseRegularServiceImpl implements FileRinseRegularService {

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Override
    public Integer add(FileRinseRegularDto fileRinseRegularDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        FileRinseRegularEntity record = new FileRinseRegularEntity();
        PublicUtils.trans(fileRinseRegularDto,record);
        try {
            result = fileRinseRegularMapper.insert(record);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"保存表file_rinse_regular出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<FileRinseRegularDto> findFileRinseDetailByFileRinseDetailId(Long fileRinseDetailId, List<Error> errorList) throws IllegalAccessException {
        List<FileRinseRegularDto> fileRinseRegularDtoList = new ArrayList<>();
        List<FileRinseRegularEntity> fileRinseRegularEntityList = null;
        try {
             fileRinseRegularEntityList = fileRinseRegularMapper.selectByFileRinseDetailId(fileRinseDetailId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_rinse_regular出错",e.toString()));
        }
        for(FileRinseRegularEntity FileRinseRegularEntity : fileRinseRegularEntityList){
            FileRinseRegularDto fileRinseRegularDto = new FileRinseRegularDto();
            PublicUtils.trans(FileRinseRegularEntity,fileRinseRegularDto);
            fileRinseRegularDtoList.add(fileRinseRegularDto);
        }
        return fileRinseRegularDtoList;
    }

    @Override
    public Integer deleteByPrimaryKey(Long id, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        try {
            result = fileRinseRegularMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_rinse_regular出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer deleteByFileRinseDetailId(Long fileRinseDetailId, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        try {
            result = fileRinseRegularMapper.deleteByFileRinseDetailId(fileRinseDetailId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_rinse_regular出错",e.toString()));
        }
        return result;
    }
}
