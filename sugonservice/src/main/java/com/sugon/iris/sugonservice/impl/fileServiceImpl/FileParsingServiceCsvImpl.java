package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularShowDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileParsingServiceCsv;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileParsingServiceCsvImpl implements FileParsingServiceCsv {

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FileTemplateGroupMapper fileTemplateGroupMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private SequenceMapper sequenceMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private RegularShowMapper regularShowMapper;

    /**
     * 解析csv文件并且写入mpp,并对文件和文件数据进行统计
     *
     * @param fileAttachmentId
     * @param errorList
     */
    @Override
    public boolean fileParsingCsv( Long fileAttachmentId, List<Error> errorList) throws IOException, IllegalAccessException {

        boolean result = true;
        //解析次数
        int xlsCount = 0;
        //解析次数
        int xlsxCount = 0;
        //解析次数
        int csvCount = 0;
        FileAttachmentEntity fileAttachmentEntity = getFileAttachment(fileAttachmentId, errorList);
        String path = fileAttachmentEntity.getAttachment().substring(0,fileAttachmentEntity.getAttachment().lastIndexOf("."));
        //获取解压文件夹内所有的文件
        List<File> fileList = new ArrayList<>();
        File baseFile = new File(path);
        PublicUtils.getAllFile(baseFile,fileList);
        //通过fileTemplateGroupId获取模板组
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = getFileTemplateGroupEntities( fileAttachmentEntity.getTemplateGroupId(),errorList);
        //获取模板，清洗字段组，清洗字段，树形结构
        List<FileTemplateDto> fileTemplateDtoList = null;
        if(!CollectionUtils.isEmpty(fileTemplateGroupEntityList)) {
            fileTemplateDtoList=     getFileTemplateDtoList(fileTemplateGroupEntityList,errorList);
        }

        if(CollectionUtils.isEmpty(fileList)){
            errorList.add(new Error(ErrorCode_Enum.FILE_01_001.getCode(),ErrorCode_Enum.FILE_01_001.getMessage()));
            return false;
        }

        if(CollectionUtils.isEmpty(fileTemplateDtoList)){
            errorList.add(new Error(ErrorCode_Enum.FILE_01_002.getCode(),ErrorCode_Enum.FILE_01_002.getMessage()));
            return false;
        }

        //模板与文件进行配对map（key：模板，value：对应的文件列表）
        Map<FileTemplateDto, List<File>> mapTemplate2File = template2Files(fileList, fileTemplateDtoList);

        //进行文件读取
        for(Map.Entry<FileTemplateDto, List<File>> entry : mapTemplate2File.entrySet()){
            FileTemplateDto  fileTemplateDto = entry.getKey();
            List<File> fileList4Template = entry.getValue();
            //如果之前已经存库则获取表名
            String tableName = getMppTableName(fileAttachmentEntity.getCaseId(), fileTemplateDto.getId());
        }

        //如果之前已经存库则获取表名
        return result;
    }

    //模板与文件进行配对map（key：模板，value：对应的文件列表）
    private Map<FileTemplateDto, List<File>> template2Files(List<File> fileList, List<FileTemplateDto> fileTemplateDtoList) {
        Map<FileTemplateDto, List<File>> mapTemplate2File = new HashMap<>();
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            //获取关键字
            String templateKeys = fileTemplateDto.getTemplateKey();
            if(StringUtils.isEmpty(templateKeys)){
                  continue;
            }
            List<String>  templateKeyList = Arrays.asList(templateKeys.split("&&"));
            //获取关键字排除
            String excludes = fileTemplateDto.getExclude();
            List<String> excludeList = new ArrayList<>();
            if(!StringUtils.isEmpty(excludes)){
                excludeList = Arrays.asList(excludes.split("&&"));
            }
            List<File>  fileList4Template = new ArrayList<>();
            for(File file : fileList){
                boolean flag1 = false;
                boolean flag2 = true;
                //满足关键字
                for(String templateKey : templateKeyList){
                    if(file.getName().contains(templateKey.trim())){
                        flag1 = true;
                        break;
                    }
                }
                //不被排除字段进行排除
                for(String exclude : excludeList){
                   if(file.getName().contains(exclude.trim())){
                       flag2 = false;
                       break;
                   }
                }
                if(flag1 && flag2){
                    fileList4Template.add(file);
                }
            }
            if(!CollectionUtils.isEmpty(fileList4Template)){
                mapTemplate2File.put(fileTemplateDto,fileList4Template);
            }
        }
        return mapTemplate2File;
    }

    /**
     * 如果之前已经存库则获取表名
     * @param caseId:案件编号
     * @param templateId：模板编号
     * @return
     */
    private String getMppTableName( Long caseId, Long templateId) {
        //通过模板编号、案件编号、用户编号、查询表file_detail，查看有无生成mpp表名
        FileDetailEntity fileDetailEntitySql = new FileDetailEntity();
        fileDetailEntitySql.setFileTemplateId(templateId);//设置模板id
        fileDetailEntitySql.setCaseId(caseId);
        return fileDetailMapper.selectTableName(fileDetailEntitySql);
    }


    //获取原始文件信息
    private FileAttachmentEntity getFileAttachment(Long fileAttachmentId, List<Error> errorList){
        //从数据库获取文件路径
        FileAttachmentEntity fileAttachmentEntity = null;
        try {
            fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(fileAttachmentId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_attachment出错",e.toString()));
        }
        return fileAttachmentEntity;
    }

    //通过fileTemplateGroupId获取模板组
    private List<FileTemplateGroupEntity> getFileTemplateGroupEntities( Long fileTemplateGroupId, List<Error> errorList) {
        FileTemplateGroupEntity fileTemplateGroupEntitySql = new FileTemplateGroupEntity();
        fileTemplateGroupEntitySql.setGroupId(fileTemplateGroupId);
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = new ArrayList<>();
        try {
            fileTemplateGroupEntityList = fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntitySql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_group出错",e.toString()));
        }
        return fileTemplateGroupEntityList;
    }

    //获取模板，清洗字段组，清洗字段，树形结构
   private List<FileTemplateDto>  getFileTemplateDtoList(List<FileTemplateGroupEntity> fileTemplateGroupEntityList ,List<Error> errorList) throws IllegalAccessException {
       List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
       for (FileTemplateGroupEntity fileTemplateGroupEntity : fileTemplateGroupEntityList){
           //获取模板基本信息
           FileTemplateEntity fileTemplateEntity = getFileTemplateEntity( fileTemplateGroupEntity,errorList);
           FileTemplateDto fileTemplateDto = new FileTemplateDto();
           PublicUtils.trans(fileTemplateEntity,fileTemplateDto);


           //获取该模板字段信息
           List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
           fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
           FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
           fileTemplateDetailEntity4Sql.setTemplateId(fileTemplateGroupEntity.getTemplateId());
           List<FileTemplateDetailEntity> fileTemplateDetailEntityList =  fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
           if(CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
               continue;
           }
           //字段不为空的模板，才参与解析。
           fileTemplateDtoList.add(fileTemplateDto);

           for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
               FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
               PublicUtils.trans(fileTemplateDetailEntity,fileTemplateDetailDto);
               fileTemplateDetailDtoList.add(fileTemplateDetailDto);
               //获取清洗字段
               FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailDto.getFileRinseDetailId());
               FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
               PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
               fileTemplateDetailDto.setFileRinseDetailDto(fileRinseDetailDto);
               //获取正则表达式
               List<RegularShowDto>  regularShowDtoListY = new ArrayList<>();
               List<RegularShowDto>  regularShowDtoListN = new ArrayList<>();
               fileRinseDetailDto.setRegularShowDtoListY(regularShowDtoListY);
               fileRinseDetailDto.setRegularShowDtoListY(regularShowDtoListN);
               //获取正则表达式
               List<RegularShowEntity> regularShowDtoLis = regularShowMapper.getRegularShowsByFileRinseDetailId(fileRinseDetailEntity.getId());
               if(!CollectionUtils.isEmpty(regularShowDtoLis)) {
                   for (RegularShowEntity regularShowEntity : regularShowDtoLis) {
                       RegularShowDto regularShowDto = new RegularShowDto();
                       PublicUtils.trans(regularShowEntity, regularShowDto);
                       if ("1".equals(regularShowEntity.getType())) {
                           regularShowDtoListY.add(regularShowDto);
                       }
                       if ("2".equals(regularShowEntity.getType())) {
                           regularShowDtoListN.add(regularShowDto);
                       }
                   }
               }
           }

           //获取清洗字段组
           FileRinseGroupEntity fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateDto.getFileRinseGroupId());
           FileRinseGroupDto fileRinseGroupDto = new FileRinseGroupDto();
           PublicUtils.trans(fileRinseGroupEntity,fileRinseGroupDto);
           fileTemplateDto.setFileRinseGroupDto(fileRinseGroupDto);
       }

       return fileTemplateDtoList;
   }

    //获取模板信息
    private FileTemplateEntity getFileTemplateEntity( FileTemplateGroupEntity fileTemplateGroupEntityBean,List<Error> errorList) {
        FileTemplateEntity fileTemplateEntity = null;
        try {
            fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTemplateGroupEntityBean.getTemplateId());
        }catch (Exception e){
          e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template出错",e.toString()));
        }
        return fileTemplateEntity;
    }
}
