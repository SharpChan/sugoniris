package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseDetailEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileRinseDetailService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileRinseDetailServiceImpl implements FileRinseDetailService {

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Override
    public Integer add(FileRinseDetailDto fileRinseDetailDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        FileRinseDetailEntity fileRinseDetailEntity = new FileRinseDetailEntity();
        PublicUtils.trans(fileRinseDetailDto,fileRinseDetailEntity);
        try {
            result = fileRinseDetailMapper.insert(fileRinseDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_rinse_detail出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<FileRinseDetailDto> findFileRinseDetailByGroupId(Long groupId, List<Error> errorList) throws IllegalAccessException {
        List<FileRinseDetailEntity> fileRinseDetailEntityList = null;
        List<FileRinseDetailDto> fileRinseDetailDtoList = new ArrayList<>();
        try {
            fileRinseDetailEntityList = fileRinseDetailMapper.selectByGroupId(groupId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_rinse_detail出错",e.toString()));
        }
        for(FileRinseDetailEntity fileRinseDetailEntity : fileRinseDetailEntityList){
            FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
            PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
            fileRinseDetailDtoList.add(fileRinseDetailDto);
        }
        return fileRinseDetailDtoList;
    }

    @Override
    public Integer modifyFileRinse(FileRinseDetailDto fileRinseDetailDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        FileRinseDetailEntity record = new FileRinseDetailEntity();
        PublicUtils.trans(fileRinseDetailDto,record);
        try {
            result = fileRinseDetailMapper.updateByPrimaryKeySelective(record);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_rinse_detail出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer deleteFileRinse(long id, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        try {
            result = fileRinseDetailMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_rinse_detail出错",e.toString()));
        }
        return result;
    }
}
