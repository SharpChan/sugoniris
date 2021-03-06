package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.UserGroupDao;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateGroupMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SequenceMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateGroupDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateGroupService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.*;

@Service
public class FileTemplateGroupServiceImpl implements FileTemplateGroupService {

    @Resource
    private FileTemplateGroupMapper fileTemplateGroupMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private SequenceMapper sequenceMapper;

    @Resource
    private UserGroupDao userGroupDaoImpl;

    @Override
    public List<FileTemplateGroupDto> getFileTemplateGroupDtoListByThisUserId(User user, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateGroupDto> fileTemplateGroupDtoList = new ArrayList<>();
        List<Long> userIdList = userGroupDaoImpl.getGroupUserIdList(user.getId(),errorList);
        List<FileTemplateGroupEntity> fileTemplateGroupEntitylist = new ArrayList<>();
        for(Long userId : userIdList){
            FileTemplateGroupEntity fileTemplateGroupEntity = new FileTemplateGroupEntity();
            fileTemplateGroupEntity.setUserId(userId);
            List<FileTemplateGroupEntity> fileTemplateGroupEntitylistPer = fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntity);
            if(!CollectionUtils.isEmpty(fileTemplateGroupEntitylistPer)){
                fileTemplateGroupEntitylist.addAll(fileTemplateGroupEntitylistPer);
            }
        }

        //?????????????????????
        Set<FileTemplateGroupEntity> sileTemplateGroupEntitySet = new HashSet<>(fileTemplateGroupEntitylist);
        Set<Long> groupIdSet = new  HashSet<>();
        for(FileTemplateGroupEntity fileTemplateGroupEntityBean : fileTemplateGroupEntitylist){
            groupIdSet.add(fileTemplateGroupEntityBean.getGroupId());
        }

        Map<Long, List<String>>  map = new HashMap<>();
        for(Long groupId : groupIdSet) {
            List<String> selectedCategories = new ArrayList<>();
            for (FileTemplateGroupEntity fileTemplateGroupEntityBean : fileTemplateGroupEntitylist) {
                if(groupId.equals(fileTemplateGroupEntityBean.getGroupId())) {
                    selectedCategories.add(fileTemplateGroupEntityBean.getTemplateName()+":"+fileTemplateGroupEntityBean.getTemplateId());
                }
            }
            map.put(groupId,selectedCategories);
        }

        for(FileTemplateGroupEntity fileTemplateGroupEntityRemoved : sileTemplateGroupEntitySet){
            FileTemplateGroupDto fileTemplateGroupDtoBean = new FileTemplateGroupDto();
            PublicUtils.trans(fileTemplateGroupEntityRemoved,fileTemplateGroupDtoBean);
            fileTemplateGroupDtoBean.setSelectedCategories(map.get(fileTemplateGroupEntityRemoved.getGroupId()));
            fileTemplateGroupDtoList.add(fileTemplateGroupDtoBean);
        }

        this.fileTemplateGroupDtoListSort(fileTemplateGroupDtoList);
        return fileTemplateGroupDtoList;
    }

    @Override
    public List<FileTemplateGroupDto> getFileTemplateGroupDtoList(User user, FileTemplateGroupDto fileTemplateGroupDto, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateGroupDto> fileTemplateGroupDtoList = new ArrayList<>();
        FileTemplateGroupEntity fileTemplateGroupEntity = new FileTemplateGroupEntity();
        PublicUtils.trans(fileTemplateGroupDto,fileTemplateGroupEntity);
        fileTemplateGroupEntity.setUserId(user.getId());
        List<FileTemplateGroupEntity> fileTemplateGroupEntitylist = null;
        try {
            fileTemplateGroupEntitylist = fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_template_group??????",e.toString()));
        }

        //?????????????????????
        Set<FileTemplateGroupEntity> sileTemplateGroupEntitySet = new HashSet<>(fileTemplateGroupEntitylist);
        Set<Long> groupIdSet = new  HashSet<>();
        for(FileTemplateGroupEntity fileTemplateGroupEntityBean : fileTemplateGroupEntitylist){
            groupIdSet.add(fileTemplateGroupEntityBean.getGroupId());
        }

        Map<Long, List<String>>  map = new HashMap<>();
        for(Long groupId : groupIdSet) {
            List<String> selectedCategories = new ArrayList<>();
            for (FileTemplateGroupEntity fileTemplateGroupEntityBean : fileTemplateGroupEntitylist) {
                if(groupId.equals(fileTemplateGroupEntityBean.getGroupId())) {
                    selectedCategories.add(fileTemplateGroupEntityBean.getTemplateName()+":"+fileTemplateGroupEntityBean.getTemplateId());
                }
            }
            map.put(groupId,selectedCategories);
        }

        for(FileTemplateGroupEntity fileTemplateGroupEntityRemoved : sileTemplateGroupEntitySet){
            FileTemplateGroupDto fileTemplateGroupDtoBean = new FileTemplateGroupDto();
            PublicUtils.trans(fileTemplateGroupEntityRemoved,fileTemplateGroupDtoBean);
            fileTemplateGroupDtoBean.setSelectedCategories(map.get(fileTemplateGroupEntityRemoved.getGroupId()));
            fileTemplateGroupDtoList.add(fileTemplateGroupDtoBean);
        }

        this.fileTemplateGroupDtoListSort(fileTemplateGroupDtoList);

        return fileTemplateGroupDtoList;
    }

    @Override
    public int fileTemplateGroupInsert(User user, FileTemplateGroupDto fileTemplateGroupDto, List<Error> errorList) {
        int i =0;
        if(StringUtils.isEmpty(fileTemplateGroupDto.getGroupName())){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_007.getCode(),"????????????????????????"));
        }
        if(CollectionUtils.isEmpty(fileTemplateGroupDto.getFileTemplateDtoList())){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_007.getCode(),"???????????????"));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            return i;
        }
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = new ArrayList<>();
        List<FileTemplateDto> fileTemplateDtoList = fileTemplateGroupDto.getFileTemplateDtoList();
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTemplateDto.getId());
            fileTemplateDto.setTemplateKey(fileTemplateEntity.getTemplateKey());
        }

        //???????????????????????????????????????????????????????????????????????????????????????
        checkRepert:  for(int j = 0 ; j< fileTemplateDtoList.size();j++){
            for(String key : fileTemplateDtoList.get(j).getTemplateKey().split("&&")){
                for(int k =0 ;k < fileTemplateDtoList.size();k++){
                     if(j == k){
                         continue;
                     }
                     if(fileTemplateDtoList.get(k).getTemplateKey().contains(key)){
                         boolean flag  = true;
                         for(String exclude :  fileTemplateDtoList.get(k).getExclude().split("&&")){
                             //?????????????????????????????????
                             if(key.contains(exclude)){
                                 flag = false;
                             }
                         }
                         if(flag){
                             errorList.add(new Error(ErrorCode_Enum.FILE_01_014.getCode(),ErrorCode_Enum.FILE_01_014.getMessage()+"["+fileTemplateDtoList.get(k).getTemplateName()+"]-["+fileTemplateDtoList.get(j).getTemplateName()+"]"));
                             break  checkRepert;
                         }

                     }
                }
            }
        }
        //????????????????????????????????????????????????
        if(!CollectionUtils.isEmpty(errorList)){
            return i;
        }

        Long groupId = sequenceMapper.getSeq("template_group_id");
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            FileTemplateGroupEntity fileTemplateGroupEntity = new FileTemplateGroupEntity();
            fileTemplateGroupEntity.setUserId(user.getId());
            fileTemplateGroupEntity.setGroupName(fileTemplateGroupDto.getGroupName());
            fileTemplateGroupEntity.setTemplateId(fileTemplateDto.getId());
            fileTemplateGroupEntity.setComment(fileTemplateDto.getComment());
            //??????groupId
            fileTemplateGroupEntity.setGroupId(groupId);
            fileTemplateGroupEntityList.add(fileTemplateGroupEntity);
        }
        try {
            i = fileTemplateGroupMapper.fileTemplateGrouplInsert(fileTemplateGroupEntityList);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_template_group??????",e.toString()));
        }

        return i;
    }

    @Override
    public int deleteFileTemplateGroup(String[] idArr, List<Error> errorList) {
        int count = 0;
        try{
            count =  fileTemplateGroupMapper.deleteFileTemplateGroupById(idArr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_template_group??????",e.toString()));
        }
        return count;
    }

    @Override
    public List<FileTemplateDto> getFileTemplateByFileTemplateGroupId(Long groupId, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
        FileTemplateGroupEntity fileTemplateGroupEntity4Sql = new FileTemplateGroupEntity();
        fileTemplateGroupEntity4Sql.setGroupId(groupId);
        try {
            List<FileTemplateGroupEntity> fileTemplateGroupEntityList = fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntity4Sql);
            for(FileTemplateGroupEntity fileTemplateGroupEntity : fileTemplateGroupEntityList){
                FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTemplateGroupEntity.getTemplateId());
                FileTemplateDto fileTemplateDto = new FileTemplateDto();
                PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
                fileTemplateDtoList.add(fileTemplateDto);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage(),e.toString()));
        }
        return fileTemplateDtoList;
    }

    //?????????????????????
    private void fileTemplateGroupDtoListSort(List<FileTemplateGroupDto> fileTemplateGroupDtoList) {
        //??????????????????????????????????????????
        Collections.sort(fileTemplateGroupDtoList, new Comparator<FileTemplateGroupDto>() {
            @Override
            public int compare(FileTemplateGroupDto bean1, FileTemplateGroupDto bean2) {
                long diff = bean1.getGroupId() - bean2.getGroupId();
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //?????????0
            }
        });
    }
}


