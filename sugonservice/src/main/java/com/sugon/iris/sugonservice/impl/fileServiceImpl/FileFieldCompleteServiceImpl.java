package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileFieldCompleteMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileFieldCompleteDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileFieldCompleteEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileFieldCompleteService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FileFieldCompleteServiceImpl implements FileFieldCompleteService {

    @Resource
    private FileFieldCompleteMapper fileFieldCompleteMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

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
                //组装关联关系
                String[] relations =  fileFieldCompleteDto.getFieldRelation().split("--");
                Map<String,String> relationMap = new HashMap<String,String>();
                String showRelation = "";
                for(String str : relations){
                      String[] arry = str.split("\\+\\+");
                      //通过id查字段信息
                    String dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(arry[0])).getFieldKey();
                    String source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(arry[1])).getFieldKey();
                    showRelation += dest+"++"+source+"--";
                }
                fileFieldCompleteDto.setShowRelation(showRelation);
                //获取目标字段名称
                fileFieldCompleteDto.setFieldDestName(fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fileFieldCompleteDto.getFieldDest()).getFieldKey());
                //获取源字段名称
                String[] sourceArr = fileFieldCompleteDto.getFieldSource().split("\\+\\+");
                String fieldSourceName = "";
                for(String str : sourceArr){
                    fieldSourceName += fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str)).getFieldKey()+"++";
                }
                fileFieldCompleteDto.setFieldSourceName(fieldSourceName);
                fileFieldCompleteDtoList.add(fileFieldCompleteDto);
            }
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()));
        }
        return fileFieldCompleteDtoList;
    }

    @Override
    public int saveFileFieldComplete(FileFieldCompleteDto fileFieldCompleteDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        FileFieldCompleteEntity fileFieldCompleteEntity = new FileFieldCompleteEntity();
        PublicUtils.trans(fileFieldCompleteDto,fileFieldCompleteEntity);

        //通过模板组编号和模板排序编号查询sortNo，查看sortNo是否重复
        FileFieldCompleteEntity fileFieldCompleteEntity4Sql = new FileFieldCompleteEntity();
        fileFieldCompleteEntity4Sql.setSortNo(fileFieldCompleteDto.getSortNo());
        fileFieldCompleteEntity4Sql.setFileTemplateGroupId(fileFieldCompleteDto.getFileTemplateGroupId());
        List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteList(fileFieldCompleteEntity4Sql);
        if(!CollectionUtils.isEmpty(fileFieldCompleteEntityList)){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_007.getCode(),"排序字段不允许重复！"));
            return i;
        }
        try {
          i =   fileFieldCompleteMapper.fileFieldCompleteInsert(fileFieldCompleteEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()));
        }
        return i;
    }

    @Override
    public int removeFileFieldComplete(Long id, List<Error> errorList) {
        int i = 0;
        try {
            i = fileFieldCompleteMapper.deleteFieldCompleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()));
        }
        return i;
    }

    @Override
    public boolean modifyCompletesSortNoById(Long id, String sortNo, List<Error> errorList) {

        FileFieldCompleteEntity fileFieldCompleteEntity = new FileFieldCompleteEntity();
        fileFieldCompleteEntity.setId(id);
        fileFieldCompleteEntity.setSortNo(sortNo);
        try {
            fileFieldCompleteMapper.updateByPrimaryKey(fileFieldCompleteEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()));
        }
        return false;
    }
}
