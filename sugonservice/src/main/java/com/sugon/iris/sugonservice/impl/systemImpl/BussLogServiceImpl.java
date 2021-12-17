package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SysBusinessLogMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.BusinessLogDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.BusinessLogEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.systemService.BussLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BussLogServiceImpl implements BussLogService {

    @Resource
    private SysBusinessLogMapper sysBusinessLogMapper;

    @Override
    public List<BusinessLogDto> queryLogs(List<Error> errorList) {
        List<BusinessLogDto> businessLogDtoList = new ArrayList<>();
        try {
            List<BusinessLogEntity> businessLogEntityList = sysBusinessLogMapper.getAllBusinessLogs();
            for(BusinessLogEntity businessLogEntity : businessLogEntityList){
                BusinessLogDto businessLogDto = new BusinessLogDto();
                PublicUtils.trans(businessLogEntity,businessLogDto);
                businessLogDto.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(businessLogDto.getAccessTime()));
                businessLogDtoList.add(businessLogDto);
            }
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表出错",e.toString()));
        }
        return businessLogDtoList;
    }
}
