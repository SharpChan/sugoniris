package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseGroupMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileRinseDetailService;
import com.sugon.iris.sugonservice.service.FileService.FileRinseService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileRinseServiceImpl implements FileRinseService {

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private FileRinseDetailService fileRinseDetailServiceImpl;

    @Override
    public Integer add(FileRinseGroupDto fileRinseDto, List<Error> errorList) throws IllegalAccessException {
         int result = 0;
        FileRinseGroupEntity fileRinseEntity4Sql = new FileRinseGroupEntity();
        PublicUtils.trans(fileRinseDto, fileRinseEntity4Sql);
        try {
            fileRinseGroupMapper.insert(fileRinseEntity4Sql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_rinse出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<FileRinseGroupDto> findFileRinseByUserId(Long userId, List<Error> errorList) throws IllegalAccessException {

        List<FileRinseGroupDto> fileRinseDtoList = new ArrayList<>();
        List<FileRinseGroupEntity> fileRinseEntityList = null;
        try {
            fileRinseEntityList = fileRinseGroupMapper.getFileRinsesByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_rinse出错",e.toString()));
        }
        for(FileRinseGroupEntity fileRinseEntity : fileRinseEntityList){
            FileRinseGroupDto fileRinseDto = new FileRinseGroupDto();
            PublicUtils.trans(fileRinseEntity,fileRinseDto);
            fileRinseDtoList.add(fileRinseDto);
        }
        return fileRinseDtoList;
    }

    @Override
    public Integer modifyFileRinse(FileRinseGroupDto fileRinseDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        FileRinseGroupEntity fileRinseEntity4Sql = new FileRinseGroupEntity();
        PublicUtils.trans(fileRinseDto,fileRinseEntity4Sql);
        try {
            result = fileRinseGroupMapper.updateByPrimaryKey(fileRinseEntity4Sql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_rinse出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer deleteFileRinse(long id, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        List<FileRinseDetailEntity> fileRinseDetailEntityList = null;
        try {
            result = fileRinseGroupMapper.deleteByPrimaryKey(id);
            //通过组id获取字段id
            fileRinseDetailEntityList = fileRinseDetailMapper.selectByGroupId(id);
            //删除字段以及字段配置的表达式
            for(FileRinseDetailEntity fileRinseDetailEntity : fileRinseDetailEntityList){
                fileRinseDetailServiceImpl.deleteFileRinse(fileRinseDetailEntity.getId(),errorList);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_rinse出错",e.toString()));
        }
        return result;
    }
}
