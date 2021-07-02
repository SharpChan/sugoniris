package com.sugon.iris.sugonservice.impl.configServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.config.SysDictionaryDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.configDtos.SysDictionaryDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.SysDictionaryEntity;
import com.sugon.iris.sugonservice.service.configService.SysDictionaryService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {

    @Resource
    private SysDictionaryDaoIntf sysDictionaryDaoImpl;


    @Override
    public List<SysDictionaryDto> getAllSysDictionaries(List<Error> errorList) throws IllegalAccessException {
        List<SysDictionaryDto> sysDictionaryDtoList= new ArrayList<>();
        List<SysDictionaryEntity> sysDictionaryEntityList = sysDictionaryDaoImpl.findSysDictionary(null,errorList);
        for(SysDictionaryEntity sysDictionaryEntity : sysDictionaryEntityList){
            SysDictionaryDto sysDictionaryDto = new SysDictionaryDto();
            PublicUtils.trans(sysDictionaryEntity,sysDictionaryDto);
            sysDictionaryDtoList.add(sysDictionaryDto);
        }
        return sysDictionaryDtoList;
    }

    @Override
    public List<SysDictionaryDto> getSysDictionariesByDicGroup(String dicGroup,List<Error> errorList) throws IllegalAccessException {
        List<SysDictionaryDto> sysDictionaryDtoList= new ArrayList<>();
        List<SysDictionaryEntity> sysDictionaryEntityList = sysDictionaryDaoImpl.findSysDictionary(dicGroup,errorList);
        for(SysDictionaryEntity sysDictionaryEntity : sysDictionaryEntityList){
            SysDictionaryDto sysDictionaryDto = new SysDictionaryDto();
            PublicUtils.trans(sysDictionaryEntity,sysDictionaryDto);
            sysDictionaryDtoList.add(sysDictionaryDto);
        }
        return sysDictionaryDtoList;
    }

    @Override
    public int updateSysDictionary(SysDictionaryDto sysDictionaryDto,List<Error> errorList) throws IllegalAccessException {
        SysDictionaryEntity sysDictionaryEntity = new SysDictionaryEntity();
        PublicUtils.trans(sysDictionaryDto,sysDictionaryEntity);
        return sysDictionaryDaoImpl.updateSysDictionary(sysDictionaryEntity,errorList);
    }

    @Override
    public int saveSysDictionary(SysDictionaryDto sysDictionaryDto,List<Error> errorList) throws IllegalAccessException {
        SysDictionaryEntity sysDictionaryEntity = new SysDictionaryEntity();
        PublicUtils.trans(sysDictionaryDto,sysDictionaryEntity);
        return sysDictionaryDaoImpl.saveSysDictionary(sysDictionaryEntity,errorList);
    }

    @Override
    public int deleteSysDictionary(Long id, List<Error> errorList) {
        return sysDictionaryDaoImpl.deleteSysDictionary(id,errorList);
    }
}
