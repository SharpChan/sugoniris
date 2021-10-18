package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDataGroupDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.UserEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileDataGroupDetailService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDataGroupDetailServiceImpl implements FileDataGroupDetailService {

    @Resource
    private FileDataGroupDetailMapper fileDataGroupDetailMapper;

    @Override
    public List<UserDto> findFileDataGroupUsersByGroupId(Long groupId, List<Error> errorList) {
        List<UserDto> userDtoList = new ArrayList<>();
        try {
            List<UserEntity> userEntityList = fileDataGroupDetailMapper.findFileDataGroupUsersByGroupId(groupId);
            userEntityList.stream().forEach(a -> {
                try {
                    userDtoList.add(PublicUtils.trans(a,new UserDto()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user,file_data_group_detail出错",e.toString()));
        }
        return userDtoList;
    }

    @Override
    public List<UserDto> findUsersNotInDataGroupsByUserId(Long groupId, List<Error> errorList) {
        List<UserDto> userDtoList = new ArrayList<>();
        try {
            List<UserEntity> userEntityList = fileDataGroupDetailMapper.findUsersNotInDataGroupsByUserId(groupId);
            userEntityList.stream().forEach(a -> {
                try {
                    userDtoList.add(PublicUtils.trans(a,new UserDto()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user,file_data_group_detail出错",e.toString()));
        }
        return userDtoList;
    }

    @Override
    public Integer saveUserFromDataGroupDetail(User user,List<FileDataGroupDetailDto> fileDataGroupDetailDtoList, List<Error> errorList) {
        int i=0;
        List<FileDataGroupDetailEntity> fileDataGroupDetailEntityList  = new ArrayList<>();
        fileDataGroupDetailDtoList.stream().forEach(a -> {
            try {
                a.setCreateUserId(user.getId());
                fileDataGroupDetailEntityList.add(PublicUtils.trans(a,new FileDataGroupDetailEntity()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        try {
            i=fileDataGroupDetailMapper.saveUserFromDataGroupDetail(fileDataGroupDetailEntityList);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_data_group_detail出错",e.toString()));
        }
        return i;
    }

    @Override
    public Integer deleteUserFromDataGroup(List<FileDataGroupDetailDto> fileDataGroupDetailDtoList, List<Error> errorList) {
        int i=0;
        List<FileDataGroupDetailEntity> fileDataGroupDetailEntityList  = new ArrayList<>();
        fileDataGroupDetailDtoList.stream().forEach(a -> {
            try {
                fileDataGroupDetailEntityList.add(PublicUtils.trans(a,new FileDataGroupDetailEntity()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        try {
            fileDataGroupDetailEntityList.stream().forEach(a -> fileDataGroupDetailMapper.deleteFileDataGroupDetail(a));
            i = fileDataGroupDetailEntityList.size();
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_data_group_detail出错",e.toString()));
        }
        return i;
    }
}
