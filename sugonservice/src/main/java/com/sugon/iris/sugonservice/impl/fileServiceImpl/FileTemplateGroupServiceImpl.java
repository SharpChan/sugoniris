package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
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
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_group出错",e.toString()));
        }

        //获取模板组编号
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
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = new ArrayList<>();
        List<FileTemplateDto> fileTemplateDtoList = fileTemplateGroupDto.getFileTemplateDtoList();
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTemplateDto.getId());
            fileTemplateDto.setTemplateKey(fileTemplateEntity.getTemplateKey());
        }

        //判断组内模板是否关键字相互包含，相互包含则提示设置排除字段
        checkRepert:  for(int j = 0 ; j< fileTemplateDtoList.size();j++){
            for(String key : fileTemplateDtoList.get(j).getTemplateKey().split("&&")){
                for(int k =0 ;k < fileTemplateDtoList.size();k++){
                     if(j == k){
                         continue;
                     }
                     if(fileTemplateDtoList.get(k).getTemplateKey().contains(key)){
                         boolean flag  = true;
                         for(String exclude :  fileTemplateDtoList.get(k).getExclude().split("&&")){
                             //已经配置对应的排除字段
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
        //含有包含关系未配置排除字段则退出
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
            //获取groupId
            fileTemplateGroupEntity.setGroupId(groupId);
            fileTemplateGroupEntityList.add(fileTemplateGroupEntity);
        }
        try {
            i = fileTemplateGroupMapper.fileTemplateGrouplInsert(fileTemplateGroupEntityList);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_template_group出错",e.toString()));
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
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_template_group出错",e.toString()));
        }
        return count;
    }

    //给模板字段排序
    private void fileTemplateGroupDtoListSort(List<FileTemplateGroupDto> fileTemplateGroupDtoList) {
        //用排序字段对字段列表进行排序
        Collections.sort(fileTemplateGroupDtoList, new Comparator<FileTemplateGroupDto>() {
            @Override
            public int compare(FileTemplateGroupDto bean1, FileTemplateGroupDto bean2) {
                long diff = bean1.getGroupId() - bean2.getGroupId();
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
    }
}


