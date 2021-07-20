package com.sugon.iris.sugonservice.impl.FileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileAttachmentMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileCaseService;
import com.sugon.iris.sugonservice.service.FileService.FolderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileCaseServiceImpl implements FileCaseService {

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileTableMapper fileTableMapper;

    @Override
    public Integer saveCase(User user, FileCaseDto fileCaseDto, List<Error> errorList) throws IllegalAccessException {
        int i =0;
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        PublicUtils.trans(fileCaseDto,fileCaseEntity);
        fileCaseEntity.setUserId(user.getId());
        try {
            i = fileCaseMapper.fileCaseInsert(fileCaseEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_case出错",e.toString()));
        }
        return i;
    }

    @Override
    public Integer updateCase(User user,FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException {
        int i =0;
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        PublicUtils.trans(fileCaseDto,fileCaseEntity);
        fileCaseEntity.setUserId(user.getId());
        try {
            i = fileCaseMapper.updateFileCase(fileCaseEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_case出错",e.toString()));
        }
        return i;
    }

    @Override
    public List<FileCaseDto> selectCaseList(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException {
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        PublicUtils.trans(fileCaseDto,fileCaseEntity);
        List<FileCaseEntity> fileCaseEntityList = null;
        List<FileCaseDto> fileCaseDtoList = new ArrayList<>();
        try {
            fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_case出错",e.toString()));
        }
        if(!CollectionUtils.isEmpty(fileCaseEntityList)){
            for(FileCaseEntity fileCaseEntityBean : fileCaseEntityList){
                FileCaseDto fileCaseDtoBean = new FileCaseDto();
                fileCaseDtoList.add(PublicUtils.trans(fileCaseEntityBean,fileCaseDtoBean));
            }
        }
        return fileCaseDtoList;
    }

    @Override
    public Integer deleteCase(User user,String[] arr,List<Error> errorList) throws Exception {
        int count = 0;
        List<String> fileAttachmentIdList = new ArrayList<>();
        try {
            for (String str : arr) {
                //通过案件内部编号查询出需要删除的所有的文件编号
                List<String> fileAttachmentIds = fileAttachmentMapper.getFileAttachmentIds(str);
                if (!CollectionUtils.isEmpty(fileAttachmentIds)) {
                    fileAttachmentIdList.addAll(fileAttachmentIds);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"获取所有的fileAttachmentId出错",e.toString()));
        }
        try {
            if (!CollectionUtils.isEmpty(fileAttachmentIdList)) {
                folderServiceImpl.deleteFile(user, (String[]) fileAttachmentIdList.toArray(new String[fileAttachmentIdList.size()]), errorList);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除文件和对应的信息出错",e.toString()));
        }
        try{
            fileTableMapper.deleteFileTableByCaseId(arr);
            count =  fileCaseMapper.deleteFileCaseById(arr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_case出错",e.toString()));
        }
        return count;
    }
}
