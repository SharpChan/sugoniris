package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDataGroupMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileDataGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDataGroupServiceImpl implements FileDataGroupService {

    @Resource
    private FileDataGroupMapper fileDataGroupMapper;

    @Override
    public List<FileDataGroupDto> getFileDataGroupDtoList(User user, FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException {
        List<FileDataGroupDto> fileDataGroupDtoList = new ArrayList<>();
        FileDataGroupEntity fileDataGroupEntity = new FileDataGroupEntity();
        PublicUtils.trans(fileDataGroupDto,fileDataGroupEntity);
        fileDataGroupEntity.setUserId(user.getId());
        try {
            List<FileDataGroupEntity>  fileDataGroupEntityList = fileDataGroupMapper.findFileDataGroups(fileDataGroupEntity);
            fileDataGroupEntityList.stream().forEach(a -> {
                try {
                    fileDataGroupDtoList.add(PublicUtils.trans(a,new FileDataGroupDto()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_data_group出错",e.toString()));
        }
        return fileDataGroupDtoList;
    }

    @Override
    public int fileDataGroupInsert(User user, FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        FileDataGroupEntity fileDataGroupEntity = new FileDataGroupEntity();
        PublicUtils.trans(fileDataGroupDto,fileDataGroupEntity);
        fileDataGroupEntity.setUserId(user.getId());
        try {
            i = fileDataGroupMapper.saveFileDataGroup(fileDataGroupEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_data_group出错",e.toString()));
        }
        return i;
    }

    @Override
    public int modifyGroupInsert(FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        FileDataGroupEntity fileDataGroupEntity = new FileDataGroupEntity();
        PublicUtils.trans(fileDataGroupDto,fileDataGroupEntity);
        try {
            i = fileDataGroupMapper.updateFileDataGroup(fileDataGroupEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_data_group出错",e.toString()));
        }
        return i;
    }

    @Override
    public int deleteFileDataGroup(Long  id, List<Error> errorList) {
        int i = 0;
        try {
           i = fileDataGroupMapper.deleteFileDataGroup(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_data_group出错",e.toString()));
        }
        return i;
    }
}
