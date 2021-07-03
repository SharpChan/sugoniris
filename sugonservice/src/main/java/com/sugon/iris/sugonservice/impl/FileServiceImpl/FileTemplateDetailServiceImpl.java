package com.sugon.iris.sugonservice.impl.FileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileTemplateDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileTemplateDetailServiceImpl implements FileTemplateDetailService {

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Override
    public List<FileTemplateDetailDto> getFileTemplateDetailDtoList(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = null;
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        fileTemplateDetailEntity.setUserId(user.getId());
        fileTemplateDetailEntity.setTemplateId(fileTemplateDetailDto.getId());
        try {
            fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_detail出错",e.toString()));
        }
       if(!CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
           for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList){
               FileTemplateDetailDto fileTemplateDetailDtoBean = new FileTemplateDetailDto();
               PublicUtils.trans(fileTemplateDetailEntityBean,fileTemplateDetailDtoBean);
               fileTemplateDetailDtoList.add(fileTemplateDetailDtoBean);
           }
       }

        return fileTemplateDetailDtoList;
    }

    @Override
    public int fileTemplateDetailInsert(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        PublicUtils.trans(fileTemplateDetailDto,fileTemplateDetailEntity);
        fileTemplateDetailEntity.setUserId(user.getId());
        if (checkSortNo(errorList, fileTemplateDetailEntity)){
            return i;
        }

        try{
           i = fileTemplateDetailMapper.fileTemplateDetailInsert(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_template_detail出错",e.toString()));
        }
        return i;
    }

    @Override
    public int updateFileTemplateDetail(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        PublicUtils.trans(fileTemplateDetailDto,fileTemplateDetailEntity);
        fileTemplateDetailEntity.setUserId(user.getId());

        //判断sort_no是否在一个模板内重复
        //通过模板号查询出所有的字段
        FileTemplateDetailEntity fileTemplateDetailEntityBean = new FileTemplateDetailEntity();
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntityBean);
        for(FileTemplateDetailEntity fileTemplateDetail : fileTemplateDetailEntityList){
            if(!fileTemplateDetail.getId().equals(fileTemplateDetailDto.getId())){
               if( fileTemplateDetail.getSortNo().equals(fileTemplateDetailDto.getSortNo())){
                   errorList.add(new Error(ErrorCode_Enum.SUGON_01_002.getCode(), ErrorCode_Enum.SUGON_01_002.getMessage()));
                   return i;
               }
            }
        }


        try{
            i = fileTemplateDetailMapper.updateFileTemplateDetail(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_template_detail出错",e.toString()));
        }
        return i;
    }

    private boolean checkSortNo(List<Error> errorList, FileTemplateDetailEntity fileTemplateDetailEntity) {
        FileTemplateDetailEntity fileTemplateDetailEntityBean = new FileTemplateDetailEntity();
        fileTemplateDetailEntityBean.setSortNo(fileTemplateDetailEntity.getSortNo());
        fileTemplateDetailEntityBean.setTemplateId(fileTemplateDetailEntity.getTemplateId());
        try {
            //检查排序字段有无被占用，被占用直接报错退出
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntityBean);
            if (!CollectionUtils.isEmpty(fileTemplateDetailEntityList)) {
                errorList.add(new Error(ErrorCode_Enum.SUGON_01_002.getCode(), ErrorCode_Enum.SUGON_01_002.getMessage()));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(), "查询表file_template_detail出错", e.toString()));
        }
        return false;
    }

    @Override
    public int deleteFileTemplateDetail( String[] idArr, List<Error> errorList) {
        int count = 0;
        try{
            count =  fileTemplateDetailMapper.deleteFileTemplateDetailById(idArr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_template_detail出错",e.toString()));
        }
        return count;
    }
}
