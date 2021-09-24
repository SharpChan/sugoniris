package com.sugon.iris.sugonservice.impl.regularServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RegularDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularDetailEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.regularService.RegularDetailService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegularDetailServiceImpl implements RegularDetailService {
    @Resource
    private RegularDetailMapper regularDetailMapper;

    @Override
    public Integer addDetail(RegularDetailDto regularDetailDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RegularDetailEntity regularDetailEntity4Sql = new RegularDetailEntity();
        PublicUtils.trans(regularDetailDto,regularDetailEntity4Sql);
        try {
            result = regularDetailMapper.insert(regularDetailEntity4Sql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表regular_detail出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<RegularDetailDto> findRegularDetailsByGroupId(Long regularGroupId, List<Error> errorList) throws IllegalAccessException {
        List<RegularDetailDto> regularDetailDtoList = new ArrayList<RegularDetailDto>();
        List<RegularDetailEntity> regularDetailEntityList = null;
        try {
            regularDetailEntityList = regularDetailMapper.selectByGroupId(regularGroupId);
        }catch(Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表regular_detail出错",e.toString()));
        }
        for(RegularDetailEntity regularDetailEntity : regularDetailEntityList){
            RegularDetailDto regularDetailDto = new RegularDetailDto();
            PublicUtils.trans(regularDetailEntity,regularDetailDto);
            regularDetailDtoList.add(regularDetailDto);
        }
        return regularDetailDtoList;
    }

    @Override
    public Integer deleteDetailByPrimaryKey(Long id, List<Error> errorList) {
        int result = 0;
        try {
            result =  regularDetailMapper.deleteByPrimaryKey(id);
        }catch(Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表regular_detail出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer modifyDetailByPrimaryKey(RegularDetailDto regularDetailDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RegularDetailEntity regularDetailEntity4Sql = new RegularDetailEntity();
        PublicUtils.trans(regularDetailDto,regularDetailEntity4Sql);
        try {
            result =  regularDetailMapper.updateByPrimaryKeySelective(regularDetailEntity4Sql);
        }catch(Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表regular_detail出错",e.toString()));
        }
        return result;
    }
}
