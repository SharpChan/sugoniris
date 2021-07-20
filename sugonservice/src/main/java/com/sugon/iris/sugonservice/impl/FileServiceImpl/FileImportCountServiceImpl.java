package com.sugon.iris.sugonservice.impl.FileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugonservice.service.FileService.FileCaseService;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import com.sugon.iris.sugonservice.service.FileService.FolderService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class FileImportCountServiceImpl implements FileImportCountService {

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileDetailMapper fileDetailMapper;


    @Override
    public List<FileCaseDto> getImportCount(Long userId,List<Error> errorList) throws IllegalAccessException {
        List<FileCaseDto> fileCaseDtoList = null;
        List<FileAttachmentDto> fileAttachmentDtoList = null;
        List<FileDetailDto>  fileDetailDtoList = null;

        FileCaseDto fileCaseDto = new FileCaseDto();
        fileCaseDto.setUserId(userId);

        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setUserId(userId);

        FileDetailEntity fileDetailEntity = new FileDetailEntity();
        fileDetailEntity.setUserId(userId);
        try {
            fileCaseDtoList = fileCaseServiceImpl.selectCaseList(fileCaseDto, errorList);
            fileAttachmentDtoList = folderServiceImpl.findFileAttachmentList(fileAttachmentDto,errorList);
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntity);
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                FileDetailDto fileDetailDto = new FileDetailDto();
                PublicUtils.trans(fileDetailEntityBean ,fileDetailDto );
                fileDetailDtoList.add(fileDetailDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){
            for(FileAttachmentDto fileAttachmentDtoBean : fileAttachmentDtoList){
                if(fileCaseDtoBean.getId().equals(fileAttachmentDtoBean.getCaseId())){
                    fileCaseDtoBean.getFileAttachmentDtoList().add(fileAttachmentDtoBean);

                    for(FileDetailDto fileDetailDtoBean : fileDetailDtoList){
                         if(fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId())){
                             fileAttachmentDtoBean.getFileDetailDtoList().add(fileDetailDtoBean);
                         }
                    }
                }
            }
        }
        return fileCaseDtoList;
    }
}
