package com.sugon.iris.sugonservice.impl.regularServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RegularDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RegularGroupMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularGroupDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.regularService.RegularService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegularServiceImpl implements RegularService {
    @Resource
    private RegularGroupMapper regularGroupMapper;


    @Resource
    private RegularDetailMapper regularDetailMapper;

    @Override
    public Integer addGroup(RegularGroupDto regularGroupDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RegularGroupEntity regularGroupEntity4Sql = new RegularGroupEntity();
        PublicUtils.trans(regularGroupDto,regularGroupEntity4Sql);
        try {
            result = regularGroupMapper.insert(regularGroupEntity4Sql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表regular_group出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer modifyGroup(RegularGroupDto regularGroupDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        RegularGroupEntity regularGroupEntity4Sql = new RegularGroupEntity();
        PublicUtils.trans(regularGroupDto,regularGroupEntity4Sql);
        try {
            result = regularGroupMapper.updateByPrimaryKeySelective(regularGroupEntity4Sql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表regular_group出错",e.toString()));
        }
        return result;
    }


    @Override
    public List<RegularGroupDto> findRegularGroup( Long userId,List<Error> errorList) throws IllegalAccessException {
        List<RegularGroupDto> regularGroupDtoList = new ArrayList<>();
        List<RegularGroupEntity> regularGroupEntityList = null;
        try {
            regularGroupEntityList = regularGroupMapper.selectGroupList();
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表regular_group出错",e.toString()));
        }
        for(RegularGroupEntity regularGroupEntity : regularGroupEntityList){
            RegularGroupDto regularGroupDto = new RegularGroupDto();
            PublicUtils.trans(regularGroupEntity,regularGroupDto);
            regularGroupDtoList.add(regularGroupDto);
        }
        return regularGroupDtoList;
    }

    @Override
    public Integer deleteGroupByPrimaryKey(Long id, List<Error> errorList) {
        int result = 0;
        try {
            result = regularGroupMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表regular_group出错",e.toString()));
        }

        return result;
    }
}
