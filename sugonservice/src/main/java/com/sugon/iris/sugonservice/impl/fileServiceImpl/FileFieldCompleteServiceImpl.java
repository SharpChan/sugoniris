package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileFieldCompleteMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileFieldCompleteDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileFieldCompleteEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileFieldCompleteService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileFieldCompleteServiceImpl implements FileFieldCompleteService {

    @Resource
    private FileFieldCompleteMapper fileFieldCompleteMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Override
    public List<FileFieldCompleteDto> getFileFieldCompletesList(Long groupId, List<Error> errorList) throws IllegalAccessException {
        List<FileFieldCompleteDto> fileFieldCompleteDtoList = new ArrayList<>();
        FileFieldCompleteEntity fileFieldCompleteEntity4Sql = new FileFieldCompleteEntity();
        fileFieldCompleteEntity4Sql.setFileTemplateGroupId(groupId);
        try {
            List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteList(fileFieldCompleteEntity4Sql);
            for(FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList){
                FileFieldCompleteDto fileFieldCompleteDto = new FileFieldCompleteDto();
                PublicUtils.trans(fileFieldCompleteEntity,fileFieldCompleteDto);
                fileFieldCompleteDto.setDestFileTemplateName(fileTemplateMapper.selectFileTemplateByPrimaryKey(fileFieldCompleteDto.getDestFileTemplateId()).getTemplateName());
                fileFieldCompleteDto.setSourceFileTemplateName(fileTemplateMapper.selectFileTemplateByPrimaryKey(fileFieldCompleteDto.getSourceFileTemplateId()).getTemplateName());
                fileFieldCompleteDtoList.add(fileFieldCompleteDto);
            }
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()));
        }
        return fileFieldCompleteDtoList;
    }

    @Override
    public int saveFileFieldComplete(FileFieldCompleteDto fileFieldCompleteDto) {
        return 0;
    }

    @Override
    public int removeFileFieldComplete(Long id) {
        return 0;
    }
}
