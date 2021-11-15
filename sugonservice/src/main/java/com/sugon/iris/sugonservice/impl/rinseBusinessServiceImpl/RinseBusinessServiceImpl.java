package com.sugon.iris.sugonservice.impl.rinseBusinessServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.rinseBusinessService.RinseBusinessService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RinseBusinessServiceImpl implements RinseBusinessService {

    @Resource
    private RinseBusinessRepeatMapper rinseBusinessRepeatMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private RinseBusinessNullMapper rinseBusinessNullMapper;

    @Resource
    private RinseBusinessReplaceMapper rinseBusinessReplaceMapper;

    @Resource
    private RinseBusinessSuffixMapper rinseBusinessSuffixMapper;

    @Resource
    private RinseBusinessPrefixMapper rinseBusinessPrefixMapper;

    @Override
    public int saveRinseBusinessRepeat(RinseBusinessRepeatDto rinseBusinessRepeatDto, List<Error> errorList) {
        int result = 0;
        RinseBusinessRepeatEntity RinseBusinessRepeatEntity4Sql = new RinseBusinessRepeatEntity();
        try {
            PublicUtils.trans(rinseBusinessRepeatDto,RinseBusinessRepeatEntity4Sql);
            result = rinseBusinessRepeatMapper.saveRinseBusinessRepeat(RinseBusinessRepeatEntity4Sql);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表rinse_business_repeat出错",e.toString()));
        }
        return result;
    }

    @Override
    public int saveRinseBusinessNull(RinseBusinessNullDto rinseBusinessNullDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RinseBusinessNullEntity rinseBusinessNull = new RinseBusinessNullEntity();
        PublicUtils.trans(rinseBusinessNullDto,rinseBusinessNull);
        try {
            result = rinseBusinessNullMapper.saveRinseBusinessNull(rinseBusinessNull);
        }catch (Exception e){
            //e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"字段不允许重复配置",e.toString()));
        }
        return result;
    }

    @Override
    public int saveRinseBusinessReplace(RinseBusinessReplaceDto rinseBusinessReplaceDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RinseBusinessReplaceEntity rinseBusinessReplaceEntity4Sql = new RinseBusinessReplaceEntity();
        PublicUtils.trans(rinseBusinessReplaceDto,rinseBusinessReplaceEntity4Sql);
        try {
            result = rinseBusinessReplaceMapper.saveRinseBusinessReplace(rinseBusinessReplaceEntity4Sql);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表rinse_business_replace出错",e.toString()));
        }
        return result;
    }

    @Override
    public int saveRinseBusinessSuffix(RinseBusinessSuffixDto rinseBusinessSuffixDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RinseBusinessSuffixEntity rinseBusinessSuffixEntity4Sql = new RinseBusinessSuffixEntity();
        PublicUtils.trans(rinseBusinessSuffixDto,rinseBusinessSuffixEntity4Sql);
        try {
            result = rinseBusinessSuffixMapper.saveRinseBusinessSuffix(rinseBusinessSuffixEntity4Sql);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表rinse_business_suffix出错",e.toString()));
        }
        return result;
    }

    @Override
    public int saveRinseBusinessPrefix(RinseBusinessPrefixDto rinseBusinessPrefixDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RinseBusinessPrefixEntity rinseBusinessPrefixEntity4Sql = new RinseBusinessPrefixEntity();
        PublicUtils.trans(rinseBusinessPrefixDto,rinseBusinessPrefixEntity4Sql);
        try {
            result = rinseBusinessPrefixMapper.saveRinseBusinessPrefix(rinseBusinessPrefixEntity4Sql);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表rinse_business_prefix出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<RinseBusinessReplaceDto> getReplaceBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException {
        List<RinseBusinessReplaceDto> rinseBusinessRepeatDtoList = new ArrayList<>();
        List<RinseBusinessReplaceEntity> rinseBusinessReplaceEntityList = null;
        try {
            rinseBusinessReplaceEntityList = rinseBusinessReplaceMapper.getRinseBusinessReplaceListByTemplateId(fileTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表rinse_business_replace出错",e.toString()));
        }
        for(RinseBusinessReplaceEntity rinseBusinessReplaceEntity : rinseBusinessReplaceEntityList){
            RinseBusinessReplaceDto rinseBusinessReplaceDto = new RinseBusinessReplaceDto();
            PublicUtils.trans(rinseBusinessReplaceEntity,rinseBusinessReplaceDto);
            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessReplaceDto.getFileTemplateDetailId());
            rinseBusinessReplaceDto.setFileTemplateDetailKey(fileTemplateDetailEntity.getFieldKey());
            rinseBusinessRepeatDtoList.add(rinseBusinessReplaceDto);
        }
        return rinseBusinessRepeatDtoList;
    }

    @Override
    public List<RinseBusinessRepeatDto> getRepetBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException {
        List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList = new ArrayList<>();
        List<RinseBusinessRepeatEntity> rinseBusinessRepeatEntityList = null;
        try {
            rinseBusinessRepeatEntityList = rinseBusinessRepeatMapper.getRinseBusinessRepeatListByTemplateId(fileTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表rinse_business_repeat出错",e.toString()));
        }
        for(RinseBusinessRepeatEntity rinseBusinessRepeatEntity : rinseBusinessRepeatEntityList){
            RinseBusinessRepeatDto rinseBusinessRepeatDto = new RinseBusinessRepeatDto();
            PublicUtils.trans(rinseBusinessRepeatEntity,rinseBusinessRepeatDto);
            String fields = rinseBusinessRepeatEntity.getFields();
            String[] fieldArr = fields.split(",");
            for(String field : fieldArr){
                FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(field));
                rinseBusinessRepeatDto.getFieldList().add(fileTemplateDetailEntity.getFieldKey());
            }
            rinseBusinessRepeatDtoList.add(rinseBusinessRepeatDto);
        }
        return rinseBusinessRepeatDtoList;
    }

    @Override
    public List<RinseBusinessNullDto> getNullBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException {
        List<RinseBusinessNullDto> rinseBusinessNullDtoList = new ArrayList<>();
        List<RinseBusinessNullEntity> rinseBusinessNullEntityList = null;
        try {
            rinseBusinessNullEntityList = rinseBusinessNullMapper.getRinseBusinessNullListByTemplateId(fileTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表rinse_business_repeat出错",e.toString()));
        }
        for(RinseBusinessNullEntity rinseBusinessNullEntity : rinseBusinessNullEntityList){
            RinseBusinessNullDto rinseBusinessNullDto = new RinseBusinessNullDto();
            PublicUtils.trans(rinseBusinessNullEntity,rinseBusinessNullDto);
            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessNullDto.getFileTemplateDetailId());
            rinseBusinessNullDto.setFileTemplateDetailKey(fileTemplateDetailEntity.getFieldKey());
            rinseBusinessNullDtoList.add(rinseBusinessNullDto);
        }
        return rinseBusinessNullDtoList;
    }

    @Override
    public List<RinseBusinessSuffixDto> getSuffixBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException {
        List<RinseBusinessSuffixDto> rinseBusinessSuffixDtoList = new ArrayList<>();
        List<RinseBusinessSuffixEntity> rinseBusinessSuffixEntityList = null;
        try {
            rinseBusinessSuffixEntityList = rinseBusinessSuffixMapper.getRinseBusinessSuffixListByTemplateId(fileTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表rinse_business_suffix出错",e.toString()));
        }
        for(RinseBusinessSuffixEntity rinseBusinessSuffixEntity : rinseBusinessSuffixEntityList){
            RinseBusinessSuffixDto rinseBusinessSuffixDto = new RinseBusinessSuffixDto();
            PublicUtils.trans(rinseBusinessSuffixEntity,rinseBusinessSuffixDto);
            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessSuffixDto.getFileTemplateDetailId());
            rinseBusinessSuffixDto.setFileTemplateDetailKey(fileTemplateDetailEntity.getFieldKey());
            rinseBusinessSuffixDtoList.add(rinseBusinessSuffixDto);
        }
        return rinseBusinessSuffixDtoList;
    }

    @Override
    public List<RinseBusinessPrefixDto> getPrefixBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException {
        List<RinseBusinessPrefixDto> rinseBusinessPrefixDtoList = new ArrayList<>();
        List<RinseBusinessPrefixEntity> rinseBusinessPrefixEntityList = null;
        try {
            rinseBusinessPrefixEntityList = rinseBusinessPrefixMapper.getRinseBusinessPrefixListByTemplateId(fileTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表rinse_business_prefix出错",e.toString()));
        }
        for(RinseBusinessPrefixEntity rinseBusinessPrefixEntity : rinseBusinessPrefixEntityList){
            RinseBusinessPrefixDto rinseBusinessPrefixDto = new RinseBusinessPrefixDto();
            PublicUtils.trans(rinseBusinessPrefixEntity,rinseBusinessPrefixDto);
            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessPrefixDto.getFileTemplateDetailId());
            rinseBusinessPrefixDto.setFileTemplateDetailKey(fileTemplateDetailEntity.getFieldKey());
            rinseBusinessPrefixDtoList.add(rinseBusinessPrefixDto);
        }
        return rinseBusinessPrefixDtoList;
    }

    @Override
    public int deleteRinseBusinessRepeatById(Long id, List<Error> errorList) throws IllegalAccessException {
        int result= 0;
        try {
            result =  rinseBusinessRepeatMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表rinse_business_repeat出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteRinseBusinessNullById(Long id, List<Error> errorList) throws IllegalAccessException {
        int result= 0;
        try {
            result =  rinseBusinessNullMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表rinse_business_null出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteRinseBusinessReplaceById(Long id, List<Error> errorList) throws IllegalAccessException {
        int result= 0;
        try {
            result =  rinseBusinessReplaceMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表rinse_business_replace出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteRinseBusinessSuffixById(Long id, List<Error> errorList) throws IllegalAccessException {
        int result= 0;
        try {
            result =  rinseBusinessSuffixMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表rinse_business_suffix出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteRinseBusinessPrefixById(Long id, List<Error> errorList) throws IllegalAccessException {
        int result= 0;
        try {
            result =  rinseBusinessPrefixMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表rinse_business_prefix出错",e.toString()));
        }
        return result;
    }
}
