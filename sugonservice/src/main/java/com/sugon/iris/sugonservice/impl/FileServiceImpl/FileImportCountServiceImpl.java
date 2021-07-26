package com.sugon.iris.sugonservice.impl.FileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileParsingFailedMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import com.sugon.iris.sugonservice.service.FileService.FileCaseService;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import com.sugon.iris.sugonservice.service.FileService.FolderService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileImportCountServiceImpl implements FileImportCountService {

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;


    @Override
    public List<FileCaseDto> getImportCount(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException {
        List<FileCaseDto> fileCaseDtoList = null;
        List<FileAttachmentDto> fileAttachmentDtoList = null;
        List<FileDetailDto>  fileDetailDtoList = new ArrayList<>();


        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setUserId(fileCaseDto.getUserId());

        FileDetailEntity fileDetailEntity = new FileDetailEntity();
        fileDetailEntity.setUserId(fileCaseDto.getUserId());
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
                         if(fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && fileDetailDtoBean.getHasImport()){
                             fileAttachmentDtoBean.getFileDetailDtoList().add(fileDetailDtoBean);
                         }else if(fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && !fileDetailDtoBean.getHasImport()){
                             fileAttachmentDtoBean.getFileDetailDtoFailedList().add(fileDetailDtoBean);
                         }
                    }
                }
            }
        }
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){
            fileCaseDtoBean.rowCount();
        }
        return fileCaseDtoList;
    }

    @Override
    public List<FileParsingFailedDto> getFileParsingFailed(Long userId, Long fileDetailId,List<Error> errorList) throws IllegalAccessException {
        List<FileParsingFailedDto> fileParsingFailedDtoList = new ArrayList<>();
        FileParsingFailedEntity fileParsingFailedEntity = new FileParsingFailedEntity();
        fileParsingFailedEntity.setUserId(userId);
        fileParsingFailedEntity.setFileDetailId(fileDetailId);
        fileParsingFailedEntity.setMark(false);
        List<FileParsingFailedEntity> fileParsingFailedEntityList = fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity);
        for(FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityList){
            FileParsingFailedDto fileParsingFailedDto = new FileParsingFailedDto();
            PublicUtils.trans(fileParsingFailedEntityBean,fileParsingFailedDto);
            fileParsingFailedDtoList.add(fileParsingFailedDto);
        }

        return fileParsingFailedDtoList;
    }


}
