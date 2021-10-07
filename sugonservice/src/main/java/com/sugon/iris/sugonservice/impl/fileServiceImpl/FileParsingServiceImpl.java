package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileParsingService;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

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
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    /**
     * 解析csv文件并且写入mpp,并对文件和文件数据进行统计
     *
     * @param userId
     * @param fileAttachmentId
     */
    @Override
    public boolean fileParsing(Long userId, Long fileAttachmentId, List<Error> errorList) throws IOException {
        //解析次数
        int xlsCount = 0;
        //解析次数
        int xlsxCount = 0;
        //解析次数
        int csvCount = 0;

        //获取原始文件的信息
        List<FileAttachmentEntity> fileAttachmentEntityList = getFileAttachmentEntities(userId, fileAttachmentId, errorList);

        FileAttachmentEntity fileAttachmentEntity = fileAttachmentEntityList.get(0);//原始文件信息，只有一条
        //获取解压后的文件夹路径
        String path = fileAttachmentEntity.getAttachment().substring(0,fileAttachmentEntityList.get(0).getAttachment().lastIndexOf("."));

        //获取解压文件夹内所有的文件
        List<File> fileList = new ArrayList<>();
        File baseFile = new File(path);
        PublicUtils.getAllFile(baseFile,fileList);

        //通过模板组编号获取数据模板
        Long fileTemplateGroupId = fileAttachmentEntityList.get(0).getTemplateGroupId();

        //通过fileTemplateGroupId获取模板组
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = getFileTemplateGroupEntities(userId, fileTemplateGroupId);//没有对应关系表直接平铺


        List<FileParsingFailedEntity>  fileParsingFailedEntityListSql = new ArrayList<>();

        //通过模板id获取模板信息和模板字段信息，并用当前模板对文件依次进行解析
        for(FileTemplateGroupEntity fileTemplateGroupEntityBean : fileTemplateGroupEntityList){

            //获取模板基本信息
            FileTemplateEntity fileTemplateEntityBean = getFileTemplateEntity(userId, fileTemplateGroupEntityBean);
            String[] file_keyArr = {};
            String[] file_excludeArr = {};
            //文件名关键字
            if(StringUtils.isNotEmpty(fileTemplateEntityBean.getTemplateKey())) {
                file_keyArr = fileTemplateEntityBean.getTemplateKey().split("&&");
            }
            //文件名关键字排除
            if(StringUtils.isNotEmpty(fileTemplateEntityBean.getExclude())) {
                file_excludeArr = fileTemplateEntityBean.getExclude().split("&&");
            }
            //获取该模板字段信息
            FileTemplateDetailEntity fileTemplateDetailEntitySql = new FileTemplateDetailEntity();
            fileTemplateDetailEntitySql.setUserId(userId);
            fileTemplateDetailEntitySql.setTemplateId(fileTemplateGroupEntityBean.getTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList =  fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntitySql);

            //该模板未配置字段直接跳到下一个模板
            if(CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
                continue;
            }

            //key:sort_no排序编号；value：正则表达式列表；用于导入前数据清洗
            Map<String,List<String>>  regularMap = new HashMap<>();
            for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList){
                List<String>  regularList = new ArrayList<>();
               if(StringUtils.isNotEmpty(fileTemplateDetailEntityBean.getRegular())){
                   String[] regulars = fileTemplateDetailEntityBean.getRegular().split("&&");
                   regularList.addAll(Arrays.asList(regulars));
               }
               regularMap.put(fileTemplateDetailEntityBean.getSortNo(),regularList);
            }

            //对模板字段按排序字段进行排序
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);

            //用该模板解析对应的文件
            breakFor:  for(File file : fileList){

                boolean key_flag = false;

                //判断文件名排除字段,优先级高，命中一个直接对下一个文件进行操作
                for(String str : file_excludeArr){
                    if(StringUtils.isNotEmpty(str) && file.getName().contains(str.trim())){
                        break  breakFor;
                    }
                }
                //判断文件名字段,都不满足continue
                for(String str : file_keyArr){
                    if(StringUtils.isNotEmpty(str) && file.getName().contains(str.trim())){
                        key_flag = true;
                    }
                }

                Long fileSeq = sequenceMapper.getSeq("file_detail");
                FileDetailEntity fileDetailEntityfSql = new FileDetailEntity();
                boolean hasImport = false;
                //匹配到文件名则进行解析
                if(key_flag){
                    //数据总函数
                    Integer rowCount = 0;
                    Integer importRowCount = 0;

                    //从文件信息表中获取已有存库的mpp表名
                    String tableName = getMppTableName(userId, fileAttachmentEntity, fileTemplateGroupEntityBean);
                    //先建表再存入数据库，数据库中没有，就是mppp没有创建表。则进行建表
                    tableName = createMppTable(userId,fileDetailEntityfSql,fileAttachmentEntity, fileTemplateEntityBean, fileTemplateDetailEntityList, tableName);
                    //组装insert语句
                    String sqlInsert = getInsertSql(fileAttachmentEntity,fileTemplateDetailEntityList, tableName);

                    //进行文件解析
                    //获取文件编号
                    String fileType = "";
                    if(file.getName().contains(".csv")) {
                        hasImport = true;
                        fileType = ".csv";
                        //文件列和表列进行匹配
                        //获取文件列
                        Map<String[],Integer> feildRefIndexMap = new HashMap();//key:表字段的排序编号，value:cav列索引
                        CsvReader csvReader = new CsvReader();
                        CsvContainer csvGbk = csvReader.read(file, Charset.forName("GBK"));
                        List<CsvRow>  rows = csvGbk.getRows();
                        rowCount = rows.size()-1;
                        //获取csv和排序字段的对应关系
                        getFeildRefIndex(fileTemplateDetailEntityList, feildRefIndexMap, rows);
                        //如果没有匹配到数据,utf8-也解析一次。满足的情况下进行覆盖
                        if(CollectionUtils.isEmpty(feildRefIndexMap)){
                            rows = null;
                            csvGbk = null;//让GC尽快销毁对象
                            CsvContainer csvUtf8 = csvReader.read(file, Charset.forName("UTF-8"));
                               rows = csvUtf8.getRows();
                            getFeildRefIndex(fileTemplateDetailEntityList, feildRefIndexMap, rows);
                        }
                        //存在对应关系则进行解析，解析后组装成sql进行存库

                        if(!CollectionUtils.isEmpty(feildRefIndexMap)){
                            nextRow:  for(int i=1;i<rows.size();i++){
                                String sqlInsertExec = sqlInsert;
                                for (Map.Entry<String[], Integer> entry : feildRefIndexMap.entrySet()) {
                                    //进行正则校验
                                    boolean checkRegular = false;
                                    List<String> regulars =  regularMap.get(entry.getKey()[0]);
                                    //正则表达式不为空进行正则校验
                                    if(!CollectionUtils.isEmpty(regulars)) {
                                        for (String regular : regulars) {
                                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regular)) {
                                                checkRegular = true;
                                                break;//只要匹配一个就跳出
                                            }
                                        }
                                        if (!checkRegular) {
                                            //全都不匹配
                                            //失败记录存入失败表
                                            FileParsingFailedEntity fileParsingFailedEntitySql = new FileParsingFailedEntity();
                                            fileParsingFailedEntitySql.setRowNumber(String.valueOf(i));
                                            fileParsingFailedEntitySql.setFileDetailId(fileSeq);
                                            fileParsingFailedEntitySql.setFileTemplateId(fileTemplateGroupEntityBean.getTemplateId());
                                            fileParsingFailedEntitySql.setFileTemplateDetailId(Long.parseLong(entry.getKey()[1]));
                                            fileParsingFailedEntitySql.setContent(rows.get(i).getField(entry.getValue()));
                                            fileParsingFailedEntitySql.setCaseId(fileAttachmentEntity.getCaseId());
                                            fileParsingFailedEntitySql.setTableName(tableName);
                                            fileParsingFailedEntitySql.setUserId(userId);
                                            fileParsingFailedEntitySql.setMark(false);
                                            fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);
                                            //调到下一行
                                            continue nextRow;
                                        }
                                    }
                                   sqlInsertExec = sqlInsertExec.replace("&&"+entry.getKey()[0]+"&&",entry.getValue() == null?"": rows.get(i).getField(entry.getValue()).replaceAll("\\s*", ""));
                                }
                               mppMapper.mppSqlExec(sqlInsertExec);
                                importRowCount++;
                           }
                           //再次存入的话更改neo4j需同步状态
                           if(importRowCount>0){
                               FileTableEntity fileTableEntity = new FileTableEntity();
                               fileTableEntity.setId(fileDetailEntityfSql.getFileTableId());
                               fileTableEntity.setNeo4jInitFlag("0");
                               fileTableMapper.updateFileTable(fileTableEntity);
                           }
                        }
                        csvCount++;
                        //存入文件信息表

                    }else if(file.getName().contains(".xls")){
                        fileType = ".xls";
                        hasImport = true;

                    }else if(file.getName().contains(".xlsx")){
                        fileType = ".xlsx";
                        hasImport = true;

                    }
                    //把文件信息存入文件信息表
                    fileDetailEntityfSql.setId(fileSeq);
                    fileDetailEntityfSql.setFileTemplateId(fileTemplateGroupEntityBean.getTemplateId());
                    fileDetailEntityfSql.setUserId(userId);
                    fileDetailEntityfSql.setCaseId(fileAttachmentEntity.getCaseId());
                    fileDetailEntityfSql.setFileAttachmentId(fileAttachmentEntity.getId());
                    fileDetailEntityfSql.setFileName(file.getName());
                    fileDetailEntityfSql.setFilePath(file.getAbsolutePath());
                    fileDetailEntityfSql.setFileType(fileType);
                    fileDetailEntityfSql.setHasImport(hasImport);
                    fileDetailEntityfSql.setImportRowCount(importRowCount);
                    fileDetailEntityfSql.setRowCount(rowCount);
                    fileDetailEntityfSql.setTableName(tableName);
                    //把信息存入文件信息表
                    fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
                }
                //把为不匹配的文件信息入库
                else{
                    fileDetailEntityfSql.setId(fileSeq);
                    fileDetailEntityfSql.setUserId(userId);
                    fileDetailEntityfSql.setCaseId(fileAttachmentEntity.getCaseId());
                    fileDetailEntityfSql.setFileAttachmentId(fileAttachmentEntity.getId());
                    fileDetailEntityfSql.setFileName(file.getName());
                    fileDetailEntityfSql.setFilePath(file.getAbsolutePath());
                    fileDetailEntityfSql.setHasImport(hasImport);
                    //把信息存入文件信息表
                    fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
                }
            }
        }
        //把不满足的信息入库
        if(!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
            fileParsingFailedMapper.fileParsingFailedInsert(fileParsingFailedEntityListSql);
        }

        return true;
    }

    //获取原始文件信息
    private List<FileAttachmentEntity> getFileAttachmentEntities(Long userId, Long fileAttachmentId, List<Error> errorList) {
        FileAttachmentEntity fleAttachmentEntitySql = new FileAttachmentEntity();
        fleAttachmentEntitySql.setId(fileAttachmentId);
        fleAttachmentEntitySql.setUserId(userId);
        //从数据库获取文件路径
        List<FileAttachmentEntity> fileAttachmentEntityList = null;
        try {
            fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntitySql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_attachment出错",e.toString()));
        }
        return fileAttachmentEntityList;
    }

    //获取模板信息
    private FileTemplateEntity getFileTemplateEntity(Long userId, FileTemplateGroupEntity fileTemplateGroupEntityBean) {
        FileTemplateEntity fileTemplateEntitySql = new FileTemplateEntity();
        fileTemplateEntitySql.setUserId(userId);
        fileTemplateEntitySql.setId(fileTemplateGroupEntityBean.getTemplateId());
        List<FileTemplateEntity> fileTemplateEntityList = fileTemplateMapper.selectFileTemplateList(fileTemplateEntitySql);
        return fileTemplateEntityList.get(0);
    }



    //如果之前已经存库则获取表名
    private String getMppTableName(Long userId, FileAttachmentEntity fileAttachmentEntity, FileTemplateGroupEntity fileTemplateGroupEntityBean) {
        //通过模板编号、案件编号、用户编号、查询表file_detail，查看有无生成mpp表名
        FileDetailEntity fileDetailEntitySql = new FileDetailEntity();
        fileDetailEntitySql.setFileTemplateId(fileTemplateGroupEntityBean.getTemplateId());//设置模板id
        fileDetailEntitySql.setCaseId(fileAttachmentEntity.getCaseId());
        fileDetailEntitySql.setUserId(userId);
        return fileDetailMapper.selectTableName(fileDetailEntitySql);
    }

    //创建mpp数据表
    private String createMppTable(Long userId,FileDetailEntity fileDetailEntityfSql,FileAttachmentEntity fileAttachmentEntity, FileTemplateEntity fileTemplateEntityBean, List<FileTemplateDetailEntity> fileTemplateDetailEntityList, String tableName) {
        if(StringUtils.isEmpty(tableName)) {
            //组装建表和插入语句
            //表名="base_"+模板配置前缀+"_"+案件编号
            tableName = "base_" + fileTemplateEntityBean.getTablePrefix() + "_" + fileAttachmentEntity.getCaseId()+"_"+userId;
            String sqlCreate =  "CREATE TABLE "+tableName+" ( id serial not null,";
            for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList){
                 sqlCreate += fileTemplateDetailEntityBean.getFieldName() +" varchar NULL,";
            }
            sqlCreate += "file_attachment_id  varchar NULL);";
            mppMapper.mppSqlExec(sqlCreate);

            FileTableEntity fileTableEntity = new FileTableEntity();
            fileTableEntity.setCaseId(fileAttachmentEntity.getCaseId());
            fileTableEntity.setFileTemplateId(fileTemplateEntityBean.getId());
            fileTableEntity.setTableName(tableName);
            fileTableEntity.setUserId(userId);
            fileTableEntity.setTitle(fileAttachmentEntity.getCaseId()+"_"+fileTemplateEntityBean.getTemplateName());
            fileTableMapper.saveFileTable(fileTableEntity);
            fileDetailEntityfSql.setFileTableId(fileTableEntity.getId());
        }else{
            FileTableEntity fileTableEntity = new FileTableEntity();
            fileTableEntity.setCaseId(fileAttachmentEntity.getCaseId());
            fileTableEntity.setFileTemplateId(fileTemplateEntityBean.getId());
            fileTableEntity.setUserId(userId);
            fileTableEntity = fileTableMapper.findFileTableList(fileTableEntity).get(0);
            fileDetailEntityfSql.setFileTableId(fileTableEntity.getId());
        }
        return tableName;
    }

    //组装插入数据语句
    private String getInsertSql(FileAttachmentEntity fileAttachmentEntity,List<FileTemplateDetailEntity> fileTemplateDetailEntityList, String tableName) {
        //组装insertSql语句
        String sqlInsert = "insert into "+tableName+"(";
        String  sqlValues = "";
        for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList){
            sqlInsert += fileTemplateDetailEntityBean.getFieldName()+",";
            sqlValues += "'&&"+fileTemplateDetailEntityBean.getSortNo()+"&&',";
        }
        sqlInsert += "file_attachment_id";
        sqlValues += "'"+fileAttachmentEntity.getId()+"'";
        sqlInsert +=") values(" +sqlValues+");";
        return sqlInsert;
    }

    //获取csv和排序字段的对应关系
    private void getFeildRefIndex(List<FileTemplateDetailEntity> fileTemplateDetailEntityList, Map<String[], Integer> feildRefIndex, List<CsvRow> rows) {
           if(!CollectionUtils.isEmpty(fileTemplateDetailEntityList) && !CollectionUtils.isEmpty(rows) ){
               for(FileTemplateDetailEntity feild : fileTemplateDetailEntityList){
                       String[] idAndSortNo = new String[2];
                       idAndSortNo[0] = feild.getSortNo();
                       idAndSortNo[1] = String.valueOf(feild.getId());
                       feildRefIndex.put(idAndSortNo,null);//没有对应的表头也要保留到map中，为insertsql保留所有字段
                       String[] excludeList =feild.getExclude().split("&&") ;
                       String[] keyList = feild.getFieldKey().split("&&");
                       CsvRow csvRowHead = rows.get(0);
                       List<String> headList = csvRowHead.getFields();
                       breakFor:  for(int i=0;i<headList.size();i++){
                            if(null != excludeList && excludeList.length>0 ){
                                  for(String exclude:excludeList){
                                        if(StringUtils.isNotEmpty(exclude) && headList.get(i).contains(exclude.trim())){
                                            break  breakFor;
                                        }
                                  }
                            }
                            if(null != keyList && keyList.length>0 ){
                                  for(String key:keyList){
                                      if(StringUtils.isNotEmpty(key) && headList.get(i).contains(key.trim())){
                                          feildRefIndex.put(idAndSortNo,i);
                                          break ;
                                      }
                                  }
                            }
                       }
               }
           }
    }

    //通过fileTemplateGroupId获取模板组
    private List<FileTemplateGroupEntity> getFileTemplateGroupEntities(Long userId, Long fileTemplateGroupId) {
        FileTemplateGroupEntity fileTemplateGroupEntitySql = new FileTemplateGroupEntity();
        fileTemplateGroupEntitySql.setUserId(userId);
        fileTemplateGroupEntitySql.setGroupId(fileTemplateGroupId);
        return fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntitySql);
    }
}
