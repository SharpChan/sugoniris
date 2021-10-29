package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseGroupMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseGroupEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileTemplateServiceImpl implements FileTemplateService {

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;


    @Override
    public List<FileTemplateDto> getFileTemplateDtoList(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
        List<FileTemplateEntity> fileTemplateEntityList = null;
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        fileTemplateEntity.setUserId(user.getId());
        try{
            fileTemplateEntityList =  fileTemplateMapper.selectFileTemplateList(fileTemplateEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template出错",e.toString()));
        }
        if(!CollectionUtils.isEmpty(fileTemplateEntityList)){
            for(FileTemplateEntity fileTemplateEntityBean : fileTemplateEntityList){
                //通过清洗字段组id获取字段组信息
                FileRinseGroupEntity fileRinseGroupEntity = null;
                if(null != fileTemplateEntityBean.getFileRinseGroupId()) {
                    fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateEntityBean.getFileRinseGroupId());
                }
                FileTemplateDto fileTemplateDtoBean = new FileTemplateDto();
                PublicUtils.trans(fileTemplateEntityBean,fileTemplateDtoBean);
                if(null != fileRinseGroupEntity) {
                    fileTemplateDtoBean.setFileRinseName(fileRinseGroupEntity.getFileRinseName());
                }
                fileTemplateDtoList.add(fileTemplateDtoBean);
            }
        }
        return fileTemplateDtoList;
    }

    @Override
    public int fileTemplateInsert(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {
        if (checkKeys(fileTemplateDto, errorList)) return 0;
        int count = 0;
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        fileTemplateEntity.setUserId(user.getId());
        try{
            count =  fileTemplateMapper.fileTemplateInsert(fileTemplateEntity);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_template出错",e.toString()));
        }
        return 0;
    }

    @Override
    public int updateFileTemplate(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {

        //校验关键字，是否有包含关系，有的话提示返回
        if (checkKeys(fileTemplateDto, errorList)) return 0;

        int count = 0;
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        fileTemplateEntity.setUserId(user.getId());
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        try{
            count =  fileTemplateMapper.updateFileTemplate(fileTemplateEntity);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"更新表file_template出错",e.toString()));
        }
        return count;
    }

    //判断关键字内是包含
    private boolean checkKeys(FileTemplateDto fileTemplateDto, List<Error> errorList) {
        if(!StringUtils.isEmpty(fileTemplateDto.getTemplateKey())){
            String[] keys = fileTemplateDto.getTemplateKey().split("&&");
             for(int i = 0 ; i < keys.length ; i++){
                 for(int j = 0 ; j < keys.length ; j++){
                     if(i == j ){
                         continue;
                     }
                    if(keys[i].contains(keys[j])){
                        errorList.add(new Error(ErrorCode_Enum.FILE_01_013.getCode(),ErrorCode_Enum.FILE_01_013.getMessage()));
                        return true;
                    }
                 }
             }
        }
        return false;
    }

    @Override
    public int deleteFileTemplate(String[] idArr, List<Error> errorList) {
        int count = 0;
        try{
            count =  fileTemplateMapper.deleteFileTemplateById(idArr);
            //删除相应的详细信息
            fileTemplateDetailMapper.deleteFileTemplateDetailByTemplatId(idArr);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_template出错",e.toString()));
        }
        return count;
    }
}
