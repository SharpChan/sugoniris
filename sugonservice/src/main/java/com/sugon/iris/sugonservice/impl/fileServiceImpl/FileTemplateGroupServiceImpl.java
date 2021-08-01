package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateGroupMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SequenceMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateGroupDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileTemplateGroupService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service
public class FileTemplateGroupServiceImpl implements FileTemplateGroupService {

    @Resource
    private FileTemplateGroupMapper fileTemplateGroupMapper;

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

        return fileTemplateGroupDtoList;
    }

    @Override
    public int fileTemplateGroupInsert(User user, FileTemplateGroupDto fileTemplateGroupDto, List<Error> errorList) {
        int i =0;
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = new ArrayList<>();
        List<FileTemplateDto> fileTemplateDtoList = fileTemplateGroupDto.getFileTemplateDtoList();
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
}
