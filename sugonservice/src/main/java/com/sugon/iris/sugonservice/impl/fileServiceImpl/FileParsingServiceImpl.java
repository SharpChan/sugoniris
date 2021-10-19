package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileDoParsingService;
import com.sugon.iris.sugonservice.service.fileService.FileParsingService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileParsingServiceImpl implements FileParsingService {

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
    private FileTableMapper fileTableMapper;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private RegularDetailMapper regularDetailMapper;

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Resource
    private FileDoParsingService fileDoParsingServiceImpl;

    /**
     * 解析csv文件并且写入mpp,并对文件和文件数据进行统计
     * @param userId
     * @param fileAttachmentId
     * @param errorList
     */
    @Override
    public boolean fileParsing(Long userId, Long fileAttachmentId, List<Error> errorList) throws IOException, IllegalAccessException {

        boolean result = true;
        //解析次数，线程安全
        AtomicInteger xlsCount=new AtomicInteger(0) ;
        //解析次数，线程安全
        AtomicInteger xlsxCount = new AtomicInteger(0) ;
        //解析次数，线程安全
        AtomicInteger csvCount = new AtomicInteger(0);

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


        List<File> fileListNew = new ArrayList<>();
        fileListNew.addAll(fileList);
        /*
         *模板与文件进行配对map（key：模板，value：对应的文件列表）
         */
        Map<FileTemplateDto, List<File>> mapTemplate2File = template2Files(fileListNew, fileTemplateDtoList);
        fileDetailFailedSave(userId, fileAttachmentEntity, fileListNew);

        //创建多线程，一个模板创建一个线程,在子线程内分别入库
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        //进行文件读取
        List<File> matchFileList = new ArrayList<>();
        for(Map.Entry<FileTemplateDto, List<File>> entry : mapTemplate2File.entrySet()){
            FileTemplateDto  fileTemplateDto = entry.getKey();
            List<File> fileList4Template = entry.getValue();
            //index[0] :tableName ;index[01: tableId
            Object[] tableInfos = createMppTable(userId,fileAttachmentEntity,fileTemplateDto);
            String insertSql = getInsertSql(fileAttachmentEntity.getId(),fileAttachmentEntity.getCaseId(),fileTemplateDto.getFileTemplateDetailDtoList(),(String) tableInfos[0],fileTemplateDto.getId());


            //key:模板字段id编号；value：正则表达式列表；用于导入前数据校验
            Map<Long,FileRinseDetailDto>  regularMap = new HashMap<>();
            for(FileTemplateDetailDto fileTemplateDetailDtoBean : fileTemplateDto.getFileTemplateDetailDtoList()){
                regularMap.put(fileTemplateDetailDtoBean.getId(),fileTemplateDetailDtoBean.getFileRinseDetailDto());
            }

            //多线程执行文件读取
            executorService.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    for(File file : fileList4Template){
                        Long fileSeq = sequenceMapper.getSeq("file_detail");
                        if(file.getName().contains(".csv")){
                            csvCount.incrementAndGet();
                            try {
                                fileDoParsingServiceImpl.doParsingCsv(userId,fileAttachmentEntity.getCaseId(),
                                                                      fileTemplateDto,file,tableInfos,insertSql,regularMap,fileSeq, fileAttachmentId, errorList);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if(file.getName().contains(".xls") || file.getName().contains(".xlsx")){
                            xlsCount.incrementAndGet();
                            try {
                                fileDoParsingServiceImpl.doParsingExcel(userId, fileAttachmentEntity.getCaseId(),
                                        fileTemplateDto, file, tableInfos, insertSql, regularMap, fileSeq, fileAttachmentId, errorList);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        //进行数据清洗
                        fileDoParsingServiceImpl.doRinse(fileTemplateDto,tableInfos, fileSeq, errorList);

                    }

                }
            });
        }

        //如果之前已经存库则获取表名
        return result;
    }

    private void fileDetailFailedSave(Long userId, FileAttachmentEntity fileAttachmentEntity, List<File> fileListNew) {
        //把为不匹配的文件信息入库
        for(File fileFailed : fileListNew){
            Long fileSeq = sequenceMapper.getSeq("file_detail");
            FileDetailEntity fileDetailEntityfSql = new FileDetailEntity();
            fileDetailEntityfSql.setId(fileSeq);
            fileDetailEntityfSql.setUserId(userId);
            fileDetailEntityfSql.setCaseId(fileAttachmentEntity.getCaseId());
            if(fileFailed.getName().contains(".csv")) {
                fileDetailEntityfSql.setFileType("csv");
            } else if(fileFailed.getName().contains(".xls")){
                fileDetailEntityfSql.setFileType("xls");
            } else if(fileFailed.getName().contains(".xlsx")){
                fileDetailEntityfSql.setFileType("xlsx");
            } else{
                fileDetailEntityfSql.setFileType("");
            }
            fileDetailEntityfSql.setFileAttachmentId(fileAttachmentEntity.getId());
            fileDetailEntityfSql.setFileName(fileFailed.getName());
            fileDetailEntityfSql.setFilePath(fileFailed.getAbsolutePath());
            fileDetailEntityfSql.setRowCount(0);
            fileDetailEntityfSql.setHasImport(false);
            //把信息存入文件信息表
            fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
        }
    }

    //组装插入数据语句
    private String getInsertSql(Long fileAttachmentId ,Long caseId,List<FileTemplateDetailDto> fileTemplateDetailDtoList, String tableName,Long fileTemplateId) {
        //组装insertSql语句
        String sqlInsert = "insert into "+tableName+"(";
        String  sqlValues = "";
        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDetailDtoList){
            sqlInsert += "`"+fileTemplateDetailDto.getFieldName()+"`,";
            sqlValues += "'&&"+fileTemplateDetailDto.getId()+"&&',";
        }
        sqlInsert += "file_template_id,case_id,file_attachment_id,file_detail_id,mppId2ErrorId";
        sqlValues += "'"+fileTemplateId+"','"+caseId+"','"+fileAttachmentId+"','&&xx_file_detail_id_xx&&',"+"'&&xx_mppId2ErrorId_xx&&'";
        sqlInsert +=") values(" +sqlValues+");";
        return sqlInsert;
    }

    /**
     * 创建mpp数据表,获取表名和表id
     * @param userId
     * @param fileAttachmentEntity
     * @param fileTemplateDto
     * @return   index[0] :tableName ;index[01: tableId
     */
    private Object[] createMppTable(Long userId,FileAttachmentEntity fileAttachmentEntity, FileTemplateDto fileTemplateDto) {
        Object[] tableInfos = new Object[2];
        //如果之前已经存库则获取表名
        FileTableEntity fileTableEntity = getMppTableName(fileAttachmentEntity.getCaseId(), fileTemplateDto.getId());
        //之前没有创建表则新建
        if(null == fileTableEntity) {
            //组装建表和插入语句
            //表名="base_"+模板配置前缀+"_"+案件编号
            String tableName = "base_" + fileTemplateDto.getTablePrefix() + "_" + fileAttachmentEntity.getCaseId()+"_"+userId;
            tableInfos[0] = tableName;
            String sqlCreate =  "CREATE TABLE `"+tableName+"` ( id serial not null,"+" mppId2ErrorId int8 NULL,"+" file_detail_id int4 NULL,"+" file_template_id int4 NULL,";
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                sqlCreate += "`"+fileTemplateDetailDto.getFieldName() +"` varchar NULL,";
            }
            sqlCreate += "file_attachment_id  varchar NULL,case_id varchar NULL);";

            String index_file_template_id = "CREATE INDEX "+tableName+"_mppid2errorid_idx ON "+tableName+" USING btree (file_template_id);";
            String index_file_detail_id = "CREATE INDEX "+tableName+"_file_detail_id ON "+tableName+" USING btree (file_detail_id);";
            mppMapper.mppSqlExec(sqlCreate);
            mppMapper.mppSqlExec(index_file_template_id);
            mppMapper.mppSqlExec(index_file_detail_id);

            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(fileAttachmentEntity.getCaseId());
            fileTableEntity4Sql.setFileTemplateId(fileTemplateDto.getId());
            fileTableEntity4Sql.setTableName(tableName);
            fileTableEntity4Sql.setTitle(fileAttachmentEntity.getCaseId()+"_"+fileTemplateDto.getTemplateName());
            fileTableEntity4Sql.setUserId(userId);
            fileTableMapper.saveFileTable(fileTableEntity4Sql);
            tableInfos[1] =  fileTableEntity4Sql.getId();
        }else{
            String tableName = fileTableEntity.getTableName();
            tableInfos[0] = tableName;
            tableInfos[1] = fileTableEntity.getId();
        }
        return tableInfos;
    }

    //模板与文件进行配对map（key：模板，value：对应的文件列表）
    //把不满足的文件信息直接入库
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
            for(Iterator<File> it = fileList.iterator(); it.hasNext();){
                File file = it.next();
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
                    //把有对应模板的文件进行删除，留下没有对应模板的进行存库
                    it.remove();
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
    private FileTableEntity getMppTableName( Long caseId, Long templateId) {
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        fileTableEntity.setFileTemplateId(templateId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);
        if(CollectionUtils.isEmpty(fileTableEntityList)){
            return null;
        }
        return fileTableEntityList.get(0);
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

           //获取清洗字段组
           FileRinseGroupEntity fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateDto.getFileRinseGroupId());
           FileRinseGroupDto fileRinseGroupDto = new FileRinseGroupDto();
           PublicUtils.trans(fileRinseGroupEntity,fileRinseGroupDto);
           fileTemplateDto.setFileRinseGroupDto(fileRinseGroupDto);

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

               //如果没有配置清洗字段则跳过
               if(null == fileTemplateDetailDto.getFileRinseDetailId()){
                     continue;
               }

               //获取清洗字段
               FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailDto.getFileRinseDetailId());
               FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
               PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
               fileTemplateDetailDto.setFileRinseDetailDto(fileRinseDetailDto);
               //获取正则表达式
               List<RegularDetailDto>  regularDetailDtoListY = new ArrayList<>();
               List<RegularDetailDto>  regularDetailDtoListN = new ArrayList<>();
               //获取正则表达式
               //获取清洗字段与正则表达式的对应关系
               List<FileRinseRegularEntity> fileRinseRegularEntityList = fileRinseRegularMapper.selectByFileRinseDetailId(fileRinseDetailEntity.getId());

               if(!CollectionUtils.isEmpty(fileRinseRegularEntityList)) {
                   for (FileRinseRegularEntity fileRinseRegularEntity : fileRinseRegularEntityList) {
                       if ("1".equals(fileRinseRegularEntity.getType())) {
                           RegularDetailEntity regularDetailEntityY = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                           RegularDetailDto regularDetailDtoY = new RegularDetailDto();
                           PublicUtils.trans(regularDetailEntityY, regularDetailDtoY);
                           regularDetailDtoListY.add(regularDetailDtoY);
                       }
                       if ("2".equals(fileRinseRegularEntity.getType())) {
                           RegularDetailEntity regularDetailEntityN = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                           RegularDetailDto regularDetailDtoN = new RegularDetailDto();
                           PublicUtils.trans(regularDetailEntityN, regularDetailDtoN);
                           regularDetailDtoListY.add(regularDetailDtoN);
                       }
                   }
                   fileRinseDetailDto.setRegularDetailDtoListY(regularDetailDtoListY);
                   fileRinseDetailDto.setRegularDetailDtoListN(regularDetailDtoListN);
               }
           }
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
