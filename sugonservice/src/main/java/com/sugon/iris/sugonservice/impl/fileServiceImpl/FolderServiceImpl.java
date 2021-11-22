package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.SSHRemote.SSHConfig;
import com.sugon.iris.sugoncommon.SSHRemote.SSHServiceBs;
import com.sugon.iris.sugoncommon.fileUtils.ZipUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppErrorInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.fileBeans.FileInfoBean;
import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.MppErrorInfoEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugondomain.enums.FileType_Enum;
import com.sugon.iris.sugondomain.enums.Peripheral_Enum;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.declarService.DeclarService;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import com.jcraft.jsch.Session;

@Slf4j
@Service
public class FolderServiceImpl implements FolderService {

    private static final String RAR  = ".rar";

    private static final String ZIP  = ".zip";

    private static final String XLSX  = ".xlsx";

    private static final String XLS  = ".xls";

    private static final String CSV = ".csv";

    private final static String DETAIL = "文件删除:";

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DeclarService declarServiceImpl;

    @Resource
    private DeclarMapper declarMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private FileFieldCompleteMapper fileFieldCompleteMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private MppErrorInfoMapper mppErrorInfoMapper;



    @Override
    public void test(User user) {
        String url = "http://localhost:8668/data2Mpp/test";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("userId", user.getId());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
        RestResult<Void> response = restTemplate.postForObject(url, httpEntity, RestResult.class);
    }

    //进行数据同步
    @Override
    public void dataSync(User user,String[] selectedArr,List<Error> errorList) throws IOException {

    }

    @Override
    public int test(){
        Integer  list = mppMapper.mppSqlExecForSearchCount("Select fkjg,jyhm,dshm from base_bank_jymx_47_1000000001");
        log.info(list.toString());
        return 0;
    }

    //如果是压缩文件则进行解压
    @Override
    public void decompress(User user,String[] selectedArr,List<Error> errorList)  {
        List<FileAttachmentEntity> fileAttachmentEntityListAll = new ArrayList<>();
        for(String id : selectedArr) {
            FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
            fleAttachmentEntity.setId(Long.parseLong(id));
            fleAttachmentEntity.setUserId(user.getId());
            //通过用户编号和文件信息id获取路径
            List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity);
            if(!CollectionUtils.isEmpty(fileAttachmentEntityList)) {
                //剔除已经解压，和剔除没有配置模板组的
                for(FileAttachmentEntity fileAttachmentEntityBean : fileAttachmentEntityList){
                     if(fileAttachmentEntityBean.getHasImport() == false && null != fileAttachmentEntityBean.getTemplateGroupId()){
                         fileAttachmentEntityListAll.add(fileAttachmentEntityBean);
                     }
                     if(fileAttachmentEntityBean.getHasImport() == true){
                         errorList.add(new Error(ErrorCode_Enum.SUGON_01_003.getCode(),fileAttachmentEntityBean.getFileName()+ErrorCode_Enum.SUGON_01_003.getMessage()));
                     }
                     if( fileAttachmentEntityBean.getTemplateGroupId()== null){
                         errorList.add(new Error(ErrorCode_Enum.SUGON_01_004.getCode(),fileAttachmentEntityBean.getFileName()+ErrorCode_Enum.SUGON_01_004.getMessage()));
                     }

                }
            }
        }
        if(CollectionUtils.isEmpty(fileAttachmentEntityListAll)){
             return;
        }
        Session session = null;
        try {
            session = new SSHConfig().getSession();
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SUGON_SSH_001.getCode(),ErrorCode_Enum.SUGON_SSH_001.getMessage()));
        }
        //linux系统
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))) {
            SSHServiceBs  sSHServiceBs = new SSHServiceBs(session);
            try {
                if (!CollectionUtils.isEmpty(fileAttachmentEntityListAll)) {
                    for (FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll) {
                        //linux解压文件
                        linuxUncompress(sSHServiceBs, fileAttachmentEntity);
                        //调用远程文件解析服务进行文件读取
                        readData(user, errorList, fileAttachmentEntity);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                errorList.add(new Error(ErrorCode_Enum.SUGON_SSH_001.getCode(),ErrorCode_Enum.SUGON_SSH_001.getMessage(),e.toString()));
            }finally {
                if(null != session){
                    try {
                        sSHServiceBs.closeSession(session);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        //windows系统,部署的主服务和文件解析服务需在同一服务器上
        else{
          try {
              if (!CollectionUtils.isEmpty(fileAttachmentEntityListAll)) {
                  for (FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll) {
                      //windows解压文件
                      windowsUncompress(fileAttachmentEntity);
                      //调用远程文件解析服务进行文件读取
                      readData(user, errorList, fileAttachmentEntity);
                  }
              }
          }catch (Exception e){
              e.printStackTrace();
          }
        }

        //通过fileAttachmentEntityListAll组装入参map【key:caseId;value:模板组id】
        Map<Long,Set<Long>> caseId2TemplateGroupIdsMap = new HashMap<>();
        for(FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll){
            Set<Long> templateGroupIdList = caseId2TemplateGroupIdsMap.get(fileAttachmentEntity.getCaseId());
            if(CollectionUtils.isEmpty(templateGroupIdList)){
                Set<Long>  templateGroupIdListNew = new HashSet<>();
                templateGroupIdListNew.add(fileAttachmentEntity.getTemplateGroupId());
                caseId2TemplateGroupIdsMap.put(fileAttachmentEntity.getCaseId(),templateGroupIdListNew);
            }else{
                templateGroupIdList.add(fileAttachmentEntity.getCaseId());
            }
        }
        //进行固定补全
        this.doFixedDefinedComplete(user.getId(),caseId2TemplateGroupIdsMap,errorList);
    }

    /**
     * 进行固定数据补全
     *
     * @param  caseId2TemplateGroupIdsMap  key:caseId;value:模板组id
     */
    @Override
    public void doFixedDefinedComplete(Long userId, Map<Long,Set<Long>> caseId2TemplateGroupIdsMap, List<Error> errorList) {
       if("1".equals(PublicUtils.getConfigMap().get("file_field_complete"))) {
           for (Map.Entry<Long, Set<Long>> entry : caseId2TemplateGroupIdsMap.entrySet()) {
               Long caseId = entry.getKey();
               Set<Long> TemplateGroupIdSet = entry.getValue();
               for (Long templateGroupId : TemplateGroupIdSet) {
                   //通过模板组id，获取补全配置
                   List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteByTemplateGroupId(templateGroupId);
                   this.fileFieldCompleteEntityListSort(fileFieldCompleteEntityList);


                   //1.按照  目地模板id，源模板id，取值字段id，目标字段id，模板组id进行分组
                   Map<Complete, List<FileFieldCompleteEntity>> completeMap = new HashMap<>();
                   //归类需要清除的错误数据
                   for (FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList) {
                       //目地模板id
                       Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                       //源模板id
                       Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                       //取值字段
                       String fieldSource = fileFieldCompleteEntity.getFieldSource();
                       //目标字段
                       Long fieldDest = fileFieldCompleteEntity.getFieldDest();

                       Complete complete = new Complete();
                       complete.setDestFileTemplateId(destFileTemplateId);
                       complete.setSourceFileTemplateId(sourceFileTemplateId);
                       complete.setFieldSource(fieldSource);
                       complete.setFieldDest(fieldDest);
                       complete.setFileTemplateGroupId(templateGroupId);

                       List<FileFieldCompleteEntity> fileFieldCompleteEntityList4Map = completeMap.get(complete);
                       if (CollectionUtils.isEmpty(fileFieldCompleteEntityList4Map)) {
                           fileFieldCompleteEntityList4Map = new ArrayList<>();
                           fileFieldCompleteEntityList4Map.add(fileFieldCompleteEntity);
                           completeMap.put(complete, fileFieldCompleteEntityList4Map);
                       } else {
                           fileFieldCompleteEntityList4Map.add(fileFieldCompleteEntity);
                       }
                   }

                   //2.删除记录在错误表中的数据
                   for (Map.Entry<Complete, List<FileFieldCompleteEntity>> completeEntry : completeMap.entrySet()) {

                       List<String> getErrorsSqlList = new ArrayList<>();
                       String[] fieldSourceArr = completeEntry.getKey().getFieldSource().split("\\+\\+");
                       List<String> fieldSourceFieldNameList = new ArrayList<>();
                       for (String str : fieldSourceArr) {
                           FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str));
                           fieldSourceFieldNameList.add(fileTemplateDetailEntity.getFieldName());
                       }

                       for (String fieldSourceFieldName : fieldSourceFieldNameList) { //遍历取值字段
                           for (FileFieldCompleteEntity fileFieldCompleteEntity : completeEntry.getValue()) {
                               //目地模板id
                               Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                               //目地模板
                               FileTemplateEntity fileTemplateDestEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(destFileTemplateId);
                               //源模板id
                               Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                               //源模板
                               FileTemplateEntity fileTemplateSourceEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(sourceFileTemplateId);
                               String relations = getFieldRelation4Sql(fileFieldCompleteEntity);
                               //目标字段
                               Long fieldDest = fileFieldCompleteEntity.getFieldDest();
                               FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fieldDest);
                               String fieldDestFieldName = fileTemplateDetailEntity.getFieldName();

                               String sql = "select a.mppid2errorid as mppid2errorid from base_" + fileTemplateDestEntity.getTablePrefix() + "_" + caseId + "_" + userId + " a  where a." +
                                       fieldDestFieldName + "='' and exists(select * from base_" + fileTemplateSourceEntity.getTablePrefix() + "_" + caseId + "_" + userId
                                       + " b where " + relations + "b." + fieldSourceFieldName + "!='')";
                               getErrorsSqlList.add(sql);
                           }
                       }
                       String sql = "";
                       for (int i = 0; i < getErrorsSqlList.size(); i++) {
                           if (i == getErrorsSqlList.size() - 1) {
                               sql += getErrorsSqlList.get(i);
                           } else {
                               sql += getErrorsSqlList.get(i) + " union ";
                           }
                       }
                       List<Long> mppid2erroridList = mppMapper.mppSqlExecForSearchResInteger(sql);

                       //获取对应 mppid2errorid，数量为1的，把对应的base_biao的mppid2errorid修改为0
                       if (!CollectionUtils.isEmpty(mppid2erroridList)) {
                           for (Long mppid2errorid : mppid2erroridList) {
                               Integer count = fileParsingFailedMapper.countRecord(mppid2errorid);
                               //获取表名
                               String tableName = fileParsingFailedMapper.selectMppTableName(mppid2errorid);
                               if (count == 1) {
                                   String upsql = "update " + tableName + " set mppid2errorid = 0 where mppid2errorid = " + mppid2errorid;
                                   mppMapper.mppSqlExec(upsql);
                               }
                           }
                       }

                       //删除错误记录信息表
                       ExecutorService fileParsingFailedInsert = Executors.newFixedThreadPool(mppid2erroridList.size() / 2000 + 1);
                       int errorPackage = 0;
                       List<Long> mppid2erroridListPackage = new ArrayList<>();
                       for (int i = 0; i < mppid2erroridList.size(); i++) {
                           mppid2erroridListPackage.add(mppid2erroridList.get(i));
                           if ((++errorPackage == 2000) || (i == mppid2erroridList.size() - 1)) {
                               errorPackage = 0;
                               //多线程避免脏读
                               List<Long> deleteList = new ArrayList<>();
                               deleteList.addAll(mppid2erroridListPackage);
                               fileParsingFailedInsert.execute(new Runnable() {
                                   @Override
                                   public void run() {
                                       mppErrorInfoMapper.deleteErrorInfoListByMppid2errorid(deleteList);
                                       fileParsingFailedMapper.deleteFileParsingFailedByMppid2erroridBatch(deleteList);
                                   }
                               });
                               mppid2erroridListPackage = new ArrayList<>();
                           }
                       }
                   }
                   //进行统计数据更新

                   //4.进行数据补全
                   for (FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList) {
                       //目地模板id
                       Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                       //目地模板
                       FileTemplateEntity fileTemplateDestEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(destFileTemplateId);
                       String destTable = "base_" + fileTemplateDestEntity.getTablePrefix() + "_" + caseId + "_" + userId;
                       //源模板id
                       Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                       //源模板
                       FileTemplateEntity fileTemplateSourceEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(sourceFileTemplateId);
                       String sourceTable = "base_" + fileTemplateSourceEntity.getTablePrefix() + "_" + caseId + "_" + userId;
                       //关联关系
                       String relations = getFieldRelation4Sql(fileFieldCompleteEntity);
                       //源关联字段
                       String[] sourceFields = getSourceFields(fileFieldCompleteEntity);
                       //取值字段
                       String fieldSource = fileFieldCompleteEntity.getFieldSource();
                       String[] fieldSourceArr = fieldSource.split("\\+\\+");
                       List<String> fieldSourceFieldNameList = new ArrayList<>();
                       for (String str : fieldSourceArr) {
                           FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str));
                           fieldSourceFieldNameList.add(fileTemplateDetailEntity.getFieldName());
                       }
                       //目标字段
                       Long fieldDest = fileFieldCompleteEntity.getFieldDest();
                       FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fieldDest);
                       String fieldDestFieldName = fileTemplateDetailEntity.getFieldName();
                       try {
                           for (String str : fieldSourceFieldNameList) {
                               String updateSql = "update  " + destTable +
                                       " a  set " + fieldDestFieldName + "= b." + str + " from (select distinct " + sourceFields[1] + str + " from " + sourceTable + " where " + sourceFields[2] + str + " != '')b where " + relations + " 1=1";
                               int aa = mppMapper.mppSqlExec(updateSql);
                               log.info("更新的数据2：" + aa + "");
                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                           errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(), "固定补全数据库操作执行出错，请进行确认！"));
                       }
                   }
               }
           }
       }else{
           for (Map.Entry<Long, Set<Long>> entry : caseId2TemplateGroupIdsMap.entrySet()) {
               //mpp  error_info 进行统一删除
               List<String> mppid2erroridList = new ArrayList<>();
               Long caseId = entry.getKey();
               Set<Long> TemplateGroupIdSet = entry.getValue();
               for (Long templateGroupId : TemplateGroupIdSet) {
                   //通过模板组id，获取补全配置
                   List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteByTemplateGroupId(templateGroupId);
                   //进行排序
                   this.fileFieldCompleteEntityListSort(fileFieldCompleteEntityList);
                   for (FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList) {
                       //目地模板id
                       Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                       //目地模板
                       FileTemplateEntity fileTemplateDestEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(destFileTemplateId);
                       //目地表
                       String destTable = "base_" + fileTemplateDestEntity.getTablePrefix() + "_" + caseId + "_" + userId;
                       //源模板id
                       Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                       //源模板
                       FileTemplateEntity fileTemplateSourceEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(sourceFileTemplateId);
                       String sourceTable = "base_" + fileTemplateSourceEntity.getTablePrefix() + "_" + caseId + "_" + userId;
                       //关联关系
                       //String relations = getFieldRelation4Sql(fileFieldCompleteEntity);
                       //源关联字段/目标字段相关信息
                       Map<String,String> source2Destmap = new HashMap<>();
                       List<String> sourceList = new ArrayList<>();
                       List<String> destList = new ArrayList<>();
                       String[] sourceFields = getDestAndSourceFields(fileFieldCompleteEntity,source2Destmap,sourceList,destList);
                       //取值字段
                       String fieldSource = fileFieldCompleteEntity.getFieldSource();
                       String[] fieldSourceArr = fieldSource.split("\\+\\+");
                       List<String> fieldSourceFieldNameList = new ArrayList<>();
                       for (String str : fieldSourceArr) {
                           FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str));
                           fieldSourceFieldNameList.add(fileTemplateDetailEntity.getFieldName());
                       }
                       //目标字段
                       Long fieldDest = fileFieldCompleteEntity.getFieldDest();
                       FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fieldDest);
                       String fieldDestFieldName = fileTemplateDetailEntity.getFieldName();

                       //查询出需要补全的数据
                       for (String strSource : fieldSourceFieldNameList) {
                           String sql_01 = "select id ,mppid2errorid,"+sourceFields[3] +fieldDestFieldName +" from "+destTable +" where "+fieldDestFieldName +"=''";
                           //需要补全的数据
                           List<Map<String,Object>> list = mppMapper.mppSqlExecForSearchRtMapList(sql_01);
                           for(Map map : list){
                               //获得查询源表的条件
                               String condi = "";
                               for(int i = 0;i<sourceList.size();i++){
                                   //只有一个关联字段,源表关联字段不允许为空
                                   if(sourceList.size() == 1) {
                                       if (i == sourceList.size() - 1) {
                                           condi +=sourceList.get(i)+" != '' and "+ sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "'";
                                       } else {
                                           condi +=sourceList.get(i)+" != '' and "+ sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "' and ";
                                       }
                                   }
                                   //有多个关联字段
                                   else{
                                       if (i == sourceList.size() - 1) {
                                           condi += sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "'";
                                       } else {
                                           condi += sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "' and ";
                                       }
                                   }
                               }
                               //判断是否有多个对应数据，有多个的话，不补全，跳过
                               String sql_02 = "select count( distinct "+strSource+") from "+sourceTable  +" where "+condi+" and "+strSource+"!=''";
                               Integer resCount = mppMapper.mppSqlExecForSearchCount(sql_02);
                               if(resCount >1 || resCount == 0){
                                    continue;
                               }
                               //获取源数据
                               String sql_03 = "select  distinct "+strSource+" from "+sourceTable  +" where "+condi+" and "+strSource+"!=''";
                               List<Map<String,Object>> listSource = mppMapper.mppSqlExecForSearchRtMapList(sql_03);
                               //进行数据补全
                               String updateSQL = "Update "+destTable+" set "+fieldDestFieldName+"= '"+listSource.get(0).get(strSource)+"' where id = " +map.get("id");
                               mppMapper.mppSqlExec(updateSQL);
                               //通过字段id和mppid2errorid把表file_parsing_failed对应数据删除
                               String mppid2errorid = map.get("mppid2errorid").toString();
                               if("0".equals(mppid2errorid)){
                                  continue;
                               }
                               List<FileParsingFailedEntity> fileParsingFailedEntityList_01 = getFileParsingFailedEntities(fieldDest, mppid2errorid);
                               if(!CollectionUtils.isEmpty(fileParsingFailedEntityList_01)) {
                                   fileParsingFailedMapper.deleteFileParsingFailedByMppid2erroridAndFileField(Long.parseLong(mppid2errorid), fieldDest);
                               }
                               //查看file_parsing_failed对应的表 相应的mppid2errorid 数据是否都已经删除，删除的话，把mpp error_info对应的数据删除
                               FileParsingFailedEntity fileParsingFailedEntity = new FileParsingFailedEntity();
                               fileParsingFailedEntity.setMppId2ErrorId(Long.parseLong(mppid2errorid));
                               List<FileParsingFailedEntity> fileParsingFailedEntityList_02 = fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity);
                               if(CollectionUtils.isEmpty(fileParsingFailedEntityList_02)){
                                   mppid2erroridList.add(mppid2errorid);
                               }
                           }
                       }
                   }
               }
               if(!CollectionUtils.isEmpty(mppid2erroridList)){
                   //删除错误记录信息表
                   ExecutorService fileParsingFailedInsert = Executors.newFixedThreadPool(mppid2erroridList.size() / 1000 + 1);
                   int errorPackage = 0;
                   List<Long> mppid2erroridListPackage = new ArrayList<>();
                   for (int i = 0; i < mppid2erroridList.size(); i++) {
                       mppid2erroridListPackage.add(Long.parseLong(mppid2erroridList.get(i)));
                       if ((++errorPackage == 2000) || (i == mppid2erroridList.size() - 1)) {
                           errorPackage = 0;
                           //多线程避免脏读
                           List<Long> deleteList = new ArrayList<>();
                           deleteList.addAll(mppid2erroridListPackage);
                           fileParsingFailedInsert.execute(new Runnable() {
                               @Override
                               public void run() {
                                   mppErrorInfoMapper.deleteErrorInfoListByMppid2errorid(deleteList);
                                   fileParsingFailedMapper.deleteFileParsingFailedByMppid2erroridBatch(deleteList);
                               }
                           });
                           mppid2erroridListPackage = new ArrayList<>();
                       }
                   }
               }
           }
       }
    }

    private List<FileParsingFailedEntity> getFileParsingFailedEntities(Long fieldDest, String mppid2errorid) {
        FileParsingFailedEntity fileParsingFailedEntity4Sql = new FileParsingFailedEntity();
        fileParsingFailedEntity4Sql.setFileTemplateDetailId(fieldDest);
        fileParsingFailedEntity4Sql.setMppId2ErrorId(Long.parseLong(mppid2errorid));
        return fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity4Sql);
    }

    //获取关联关系
    private String getFieldRelation4Sql(FileFieldCompleteEntity fileFieldCompleteEntity) {
        //关联关系
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        Map<Long,Long> fieldRelationMap = new HashMap<>();
        String[] fieldRelationArr = fieldRelation.split("--");
        String relations = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
            fieldRelationMap.put(Long.parseLong(fieldArr[0]),Long.parseLong(fieldArr[1]));
            //获取目标字段字段名
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //获取源字段字段名
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));

            relations += " a."+dest.getFieldName()+" = b."+source.getFieldName() + " AND ";
        }
        return relations;
    }
    //获取目标、源关联字段
    private String[] getSourceFields(FileFieldCompleteEntity fileFieldCompleteEntity) {
        //关联关系
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        String[] fieldRelationArr = fieldRelation.split("--");
        String[] sourceFields = new String[4];
        //源带别名
        String sourceFields1 = "";
        //源不带别名
        String sourceFields2 = "";
        //源放非空判断
        String sourceFields3 = "";

        //目标查询字段
        String sourceFields4 = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
           //获取目标字段字段名
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //获取源字段字段名
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));
            sourceFields1 += " c."+source.getFieldName() + ",";
            sourceFields2 +=  source.getFieldName() + ",";
            sourceFields3 +=  source.getFieldName() + "!='' and ";
            sourceFields4 +=  dest.getFieldName()+",";
        }
        sourceFields[0] = sourceFields1;
        sourceFields[1] = sourceFields2;
        sourceFields[2] = sourceFields3;
        sourceFields[3] = sourceFields4;
        return sourceFields;
    }

    /**
     *
     * @param fileFieldCompleteEntity
     * @param map  [source,dest]
     * @param sourceList
     * @param destList
     * @return
     */
    private String[] getDestAndSourceFields(FileFieldCompleteEntity fileFieldCompleteEntity,Map<String,String> map,List<String> sourceList,List<String> destList) {
        //关联关系
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        String[] fieldRelationArr = fieldRelation.split("--");
        String[] sourceFields = new String[4];
        //源带别名
        String sourceFields1 = "";
        //源不带别名
        String sourceFields2 = "";
        //源放非空判断
        String sourceFields3 = "";

        //目标查询字段
        String sourceFields4 = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
            //获取目标字段字段名
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //获取源字段字段名
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));
            sourceFields1 += " c."+source.getFieldName() + ",";
            sourceFields2 +=  source.getFieldName() + ",";
            sourceFields3 +=  source.getFieldName() + "!='' and ";
            sourceFields4 +=  dest.getFieldName()+",";
            map.put(source.getFieldName(),dest.getFieldName());
            sourceList.add(source.getFieldName());
            destList.add(dest.getFieldName());
        }
        sourceFields[0] = sourceFields1;
        sourceFields[1] = sourceFields2;
        sourceFields[2] = sourceFields3;
        sourceFields[3] = sourceFields4;
        return sourceFields;
    }

    @Data
    private class Complete{
        //目地模板id
        private long destFileTemplateId;
        //源模板id
        private long sourceFileTemplateId;
        //取值字段id
        private String fieldSource;
        //目标字段id
        private long fieldDest;
        //模板组id
        private long fileTemplateGroupId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complete complete = (Complete) o;
            return destFileTemplateId == complete.destFileTemplateId &&
                    sourceFileTemplateId == complete.sourceFileTemplateId &&
                    fieldDest == complete.fieldDest &&
                    fileTemplateGroupId == complete.fileTemplateGroupId &&
                    Objects.equals(fieldSource, complete.fieldSource);
        }

        @Override
        public int hashCode() {
            return Objects.hash(destFileTemplateId, sourceFileTemplateId, fieldSource, fieldDest, fileTemplateGroupId);
        }
    }

    /**
     * 对固定补全配置进行排序
     * @param
     */
    private static void fileFieldCompleteEntityListSort(List<FileFieldCompleteEntity> fileFieldCompleteEntityList) {
        //用排序字段对字段列表进行排序
        Collections.sort(fileFieldCompleteEntityList, new Comparator<FileFieldCompleteEntity>() {
            @Override
            public int compare(FileFieldCompleteEntity bean1, FileFieldCompleteEntity bean2) {
                int diff = Integer.parseInt(bean1.getSortNo()) - Integer.parseInt(bean2.getSortNo());
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
    }

    private void readData(User user, List<Error> errorList, FileAttachmentEntity fileAttachmentEntity) {
        //远程调用进行数据同步
        //获取远程调用路径
        String url = null;
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))) {
            url = PublicUtils.getConfigMap().get("fileServer") + Peripheral_Enum.sugonfilerest_data2Mpp_uploadFile.getCode();
        }else{
            url = PublicUtils.getConfigMap().get("fileServer_dev") + Peripheral_Enum.sugonfilerest_data2Mpp_uploadFile.getCode();
        }

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("userId", user.getId());
        paramMap.add("fileAttachmentId", fileAttachmentEntity.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
        String resultRemote= restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        Gson gson = new Gson();
        RestResult<Void> response = gson.fromJson(resultRemote, new TypeToken<RestResult<Void>>(){}.getType());

        if(!CollectionUtils.isEmpty(response.getErrorList())){
            errorList.addAll(response.getErrorList());
        }
        //修改数据同步状态
        fileAttachmentEntity.setHasImport(true);
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
    }

    //linux下解压文件
    private void linuxUncompress(SSHServiceBs sSHServiceBs, FileAttachmentEntity fileAttachmentEntity) throws IOException {
        String command = null;
        //已经存在则删除
        command = "if [-d " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + "]; then rm -rf  " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " fi";
        sSHServiceBs.execCommand(command);
        if(!(".csv".equals(fileAttachmentEntity.getFileType()) || ".xls".equals(fileAttachmentEntity.getFileType())||".xlsx".equals(fileAttachmentEntity.getFileType()))) {
            command = "unar  -o " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " " + fileAttachmentEntity.getAttachment();
        }
        sSHServiceBs.execCommand(command);
        fileAttachmentEntity.setHasDecompress(true);
        //修改解压状态
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
    }

    //windows下解压文件
    private void windowsUncompress( FileAttachmentEntity fileAttachmentEntity) throws IOException {

        //判断文件是否已经存在，存在的话先删除
        File file = new File(fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), ""));
        deleteDir(file);
        List<String> shellList = new ArrayList<>();
        if(!(".csv".equals(fileAttachmentEntity.getFileType()) || ".xls".equals(fileAttachmentEntity.getFileType())||".xlsx".equals(fileAttachmentEntity.getFileType()))) {

            ZipUtil.unZipFiles(fileAttachmentEntity.getAttachment(),fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "\\"));
        }
        fileAttachmentEntity.setHasDecompress(true);
        //修改解压状态
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
           //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public int uploadFile(User user, List<MultipartFile> files, Long caseId, List<Error> errorList) throws Exception {
        //在配置页面配置文件的上传路径
        String uploadPath = null;
        //生产linux环境
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))) {
            uploadPath = PublicUtils.getConfigMap().get("fileUploadPath");
        }else{
            uploadPath = PublicUtils.getConfigMap().get("fileUploadPath_windows");
        }
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Calendar calendar = Calendar.getInstance();
        String year =String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        String fileServerBasePath = PublicUtils.getConfigMap().get("fileServerBasePath");
        String fileServerPAth = fileServerBasePath + "/"+year+"/"+month+"/"+day+"/"+caseId+"/";
        Session session = null;
        SSHServiceBs sSHServiceBs = null;
        //生产linux环境
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))){
            session = new SSHConfig().getSession();
            sSHServiceBs = new SSHServiceBs(session);
            sSHServiceBs.listFiles(fileServerPAth,true);
            //文件上传组件
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            int i = 0;
            List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
            //把文件临时存入服务器，并且把文件信息记录进表。并且上传到
            for (MultipartFile fileItem : files) {
                String fileName = fileItem.getOriginalFilename();
                String md5FileName = DigestUtils.md5DigestAsHex((fileName+user.getId()+System.currentTimeMillis()+new Random().nextInt(99)).getBytes());
                String size =String.valueOf(fileItem.getSize());
                String type= "";

                if(fileName.contains(".")){
                    String str1=fileName.substring(0, fileName.indexOf("."));
                    type=fileName.substring(str1.length(), fileName.length());
                    if(!PublicUtils.getConfigMap().get("fileType").contains(type)){
                        errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                        continue;
                    }
                }else {
                    errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                    continue;
                }

                String attachment = null;

                if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)){

                    fileServerPAth = fileServerPAth + md5FileName+"/";
                    sSHServiceBs.makeFiles(fileServerPAth);
                    attachment = fileServerPAth;
                }else{
                    attachment = fileServerPAth+md5FileName+type;
                }
                FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
                fileAttachmentEntity.setAttachment(attachment);
                fileAttachmentEntity.setCaseId(caseId);
                fileAttachmentEntity.setFileName(fileName);
                fileAttachmentEntity.setFileType(type);
                fileAttachmentEntity.setFileSize(size);
                fileAttachmentEntity.setUserId(user.getId());
                fileAttachmentEntity.setHasDecompress(false);
                fileAttachmentEntity.setHasImport(false);
                //如果是经侦角色，则默认模板组
                if(user.isEconomicUser()){
                    fileAttachmentEntity.setTemplateGroupId(1L);
                }
                fileAttachmentEntityList.add(fileAttachmentEntity);

                FileOutputStream fout = null;
                InputStream is = null;
                try {
                    //io 输入流读文件
                    is = fileItem.getInputStream();
                    //利用输出流写入对应的文件夹
                    if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)) {
                        fout = new FileOutputStream(new File(uploadPath + "/" + fileName));
                    }else{
                        fout = new FileOutputStream(new File(uploadPath + "/" + md5FileName + type));
                    }
                    //写入数据
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = (is.read(buffer))) > -1) {
                        fout.write(buffer, 0, len);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorList.add(new Error(ErrorCode_Enum.SYS_STORE_001.getCode(),ErrorCode_Enum.SYS_STORE_001.getMessage(),fileName));
                }finally {
                    if(null!= fout){
                        try{
                            fout.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(null!= fout){
                        try{
                            is.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                //文件上传到文件服务器，使用一次SSHServiceBs对象创建一次，可能有文件不被释放的情况
                if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)){
                    sSHServiceBs.uploadFile(fileServerPAth+fileName,uploadPath + "/" + fileName);
                }else{
                    sSHServiceBs.uploadFile(fileServerPAth+md5FileName+type,uploadPath + "/" + md5FileName+type);
                }
                sSHServiceBs.closeSession(session);
                //删除本地文件
                File file = new File(uploadPath + "/" + md5FileName+type);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                i++;
            }
            //把文件信息入库
            fileAttachmentMapper.batchFileAttachmentInsert(fileAttachmentEntityList);
            return i;
        }
        //测试windows环境
        else{
            //文件上传组件
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            int i = 0;
            List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
            //把文件临时存入服务器，并且把文件信息记录进表。并且上传到
            for (MultipartFile fileItem : files) {
                String fileName = fileItem.getOriginalFilename();
                String md5FileName = DigestUtils.md5DigestAsHex((fileName+user.getId()+System.currentTimeMillis()+new Random().nextInt(99)).getBytes());
                String size =String.valueOf(fileItem.getSize());
                String type= "";

                if(fileName.contains(".")){
                    String str1=fileName.substring(0, fileName.indexOf("."));
                    type=fileName.substring(str1.length(), fileName.length());
                    if(!PublicUtils.getConfigMap().get("fileType").contains(type)){
                        errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                        continue;
                    }
                }else {
                    errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                    continue;
                }

                String attachment = null;

                if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)){
                    fileServerPAth = uploadPath + "/" +fileName+"/";
                    attachment = fileServerPAth;
                }else{
                    attachment = uploadPath+"/"+md5FileName+type;
                }
                FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
                fileAttachmentEntity.setAttachment(attachment);
                fileAttachmentEntity.setCaseId(caseId);
                fileAttachmentEntity.setFileName(fileName);
                fileAttachmentEntity.setFileType(type);
                fileAttachmentEntity.setFileSize(size);
                fileAttachmentEntity.setUserId(user.getId());
                fileAttachmentEntity.setHasDecompress(false);
                fileAttachmentEntity.setHasImport(false);
                //如果是经侦角色，则默认模板组
                if(user.isEconomicUser()){
                    fileAttachmentEntity.setTemplateGroupId(1L);
                }
                fileAttachmentEntityList.add(fileAttachmentEntity);

                FileOutputStream fout = null;
                InputStream is = null;
                try {
                    //io 输入流读文件
                    is = fileItem.getInputStream();
                    //利用输出流写入对应的文件夹
                    if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)) {
                        fout = new FileOutputStream(new File(uploadPath + "/" + fileName));
                    }else{
                        fout = new FileOutputStream(new File(uploadPath + "/" + md5FileName + type));
                    }
                    //写入数据
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = (is.read(buffer))) > -1) {
                        fout.write(buffer, 0, len);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorList.add(new Error(ErrorCode_Enum.SYS_STORE_001.getCode(),ErrorCode_Enum.SYS_STORE_001.getMessage(),fileName));
                }finally {
                    if(null!= fout){
                        try{
                            fout.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(null!= fout){
                        try{
                            is.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                i++;
            }
            //把文件信息入库
            fileAttachmentMapper.batchFileAttachmentInsert(fileAttachmentEntityList);
            return i;
        }
    }

    @Override
    public int deleteFile(User user, String[] arr,boolean flag ,List<Error> errorList) throws Exception {
        List<String> arrList = new ArrayList<>(Arrays.asList(arr));
        int j = 0;
        List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
        if(flag && StringUtils.isNotEmpty(PublicUtils.getConfigMap().get("file_delete_time"))) {
            String time = PublicUtils.getConfigMap().get("file_delete_time").trim().replaceAll("^\\s*$", "");
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            if (StringUtils.isNotEmpty(time) && pattern.matcher(time).matches()) {

                for (Iterator<String> it = arrList.iterator(); it.hasNext();) {
                    String   id = it.next();
                    FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
                    fleAttachmentEntity.setId(Long.parseLong(id));
                    fleAttachmentEntity.setUserId(user.getId());
                    //查询到文件服务器的文件路径进行删除
                    FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity).get(0);
                    Long createTime = fileAttachmentEntity.getCreateTime().getTime();
                    Long nowTime = new Date().getTime();

                    if ((nowTime - createTime) / 1000 / 60 / 60 > Long.parseLong(time)) {
                        DeclarationDetailDto declarationDetailDto = new DeclarationDetailDto();
                        declarationDetailDto.setDetail(DETAIL + fileAttachmentEntity.getFileName());
                        declarationDetailDto.setOwnerUserId(user.getId());
                        declarationDetailDto.setStatus(PublicRuleUtils.ZERO);
                        declarationDetailDto.setBusinessType(PublicRuleUtils.TWO);
                        declarationDetailDto.setBusinessId(fileAttachmentEntity.getId());
                        declarationDetailDtoList.add(declarationDetailDto);
                        //删除数组中对应的值
                        it.remove();
                        j++;
                    }
                }

                try{
                    if(!CollectionUtils.isEmpty(declarationDetailDtoList)) {
                        declarServiceImpl.saveDeclaration(user, declarationDetailDtoList, errorList);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表declaration出错",e.toString()));
                }

                if (arrList.size() == 0) {
                    return j;
                }
            }
        }
        arr  = (String[])arrList.toArray(new String[arrList.size()]);

        int i = 0;
        for(String id :arr ){
            //查询到文件服务器的文件路径进行删除
            FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(Long.parseLong(id));
            if(null == fileAttachmentEntity){
              return  j;
            }
            Session session = new SSHConfig().getSession();
            SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
            sSHServiceBs = new SSHServiceBs(new SSHConfig().getSession());
            //删除压缩文件
            //sSHServiceBs.deleteFile(fileAttachmentEntity.getAttachment());

            //删除解压文件
            String path = null;
            if(fileAttachmentEntity.getAttachment().contains(".")) {
                 path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf("."));
            }else if(fileAttachmentEntity.getAttachment().contains("/")){
                path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf(""));
            }
            String command = "rm -rf  " + path;
            sSHServiceBs.execCommand(command);
            //关闭session
            sSHServiceBs.closeSession(session);
            //删除mpp数据库的数据
            //通过fleAttachmentEntity的id编号获取文件信息
            FileDetailEntity fileDetailEntitySql = new FileDetailEntity();
            fileDetailEntitySql.setFileAttachmentId(fileAttachmentEntity.getId());
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntitySql);
            //删除mpp数据
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                if(StringUtils.isEmpty(fileDetailEntityBean.getTableName())){
                    continue;
                }
                //删除资源表数据
                String sql = "delete from  "+fileDetailEntityBean.getTableName()+" where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
                mppMapper.mppSqlExec(sql);
                //删除原始表数据
                String originSql = "delete from  "+fileDetailEntityBean.getOriginTableName()+" where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
                mppMapper.mppSqlExec(originSql);
            }
            //删除文件信息
            fileDetailMapper.deleteFileDetailByFileAttachmentId(fileAttachmentEntity.getId());
            fileAttachmentMapper.deleteFileAttachmentById(fileAttachmentEntity);
            //删除校验不通过信息
            fileParsingFailedMapper.deleteFileParsingFailedByFileAttachmentId(fileAttachmentEntity.getId());
            //删除mpp数据库内error_info数据
            String sql =  "delete from error_info where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
            mppMapper.mppSqlExec(sql);
        }
        return i+j;
    }

    @Override
    public List<FileAttachmentDto> findFileAttachmentList(FileAttachmentDto fileAttachmentDto , List<Error> errorList) throws IllegalAccessException {
        List<FileAttachmentDto> FileAttachmentDtoList = new ArrayList<>();
        FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
        PublicUtils.trans(fileAttachmentDto,fleAttachmentEntity);
        List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity);
        for(FileAttachmentEntity fileAttachmentEntity: fileAttachmentEntityList){
            FileAttachmentDto fileAttachmentDtoBean = new FileAttachmentDto();
            //查询申报状态
            DeclarationDetailEntity declarationDetailEntitySql = new DeclarationDetailEntity();
            declarationDetailEntitySql.setBusinessType(PublicRuleUtils.TWO);
            declarationDetailEntitySql.setBusinessId(fileAttachmentEntity.getId());
            List<DeclarationDetailEntity> declarationDetailEntityList = declarMapper.findDeclarationDetail(declarationDetailEntitySql);
            if(CollectionUtils.isEmpty(declarationDetailEntityList)){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.ZERO);
            }else if(PublicRuleUtils.ZERO.equals(declarationDetailEntityList.get(0).getStatus())){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.ONE);
            }else if(PublicRuleUtils.TWO.equals(declarationDetailEntityList.get(0).getStatus())){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.TWO);
            }

            PublicUtils.trans(fileAttachmentEntity,fileAttachmentDtoBean);
            FileAttachmentDtoList.add(fileAttachmentDtoBean);
        }
        return FileAttachmentDtoList;
    }

    @Override
    public Integer csvReadOperation(String userId, Long caseId, String filePath) {


        return null;
    }

    /**
     * 修改模板组
     *
     * @param user
     * @param fileAttachmentDto
     * @param errorList
     */
    @Override
    public Integer updateFileAttachmentTemplateGroup(User user, FileAttachmentDto fileAttachmentDto, List<Error> errorList) throws IllegalAccessException {

        FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
        PublicUtils.trans(fileAttachmentDto,fileAttachmentEntity);
        fileAttachmentEntity.setUserId(user.getId());
        fileAttachmentMapper.updateFileAttachmentTemplateGroup(fileAttachmentEntity);
        return null;
    }

    @Override
    public String getFileServerIp() {
        if("1".equals(PublicUtils.getConfigMap().get("environment"))){
            return "localhost:8668";
        }
        return PublicUtils.getConfigMap().get("webSocket_fileRest");
    }




    /**
     *
     * @param file:指定文件夹
     * @param fileType：是否只取压缩文件 0：除压缩包；1：除普通csv、Excel、Excels；2：两者都有
     */
    private List<FileInfoBean> getFileNameAndPath(File file , String fileType) {
        //存放文件名和它的绝对路径
        List<FileInfoBean> fileList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File tmpFile :files){
            if (tmpFile.isDirectory()){//当前是文件夹 继续递归
                getFileNameAndPath(tmpFile,fileType);
            }else {//当前是文件
                //获取最后一个.的位置
                int lastIndexOf = tmpFile.getAbsolutePath().lastIndexOf(".");
                //获取文件的后缀名
                String suffix = tmpFile.getAbsolutePath().substring(lastIndexOf);

                if(PublicRuleUtils.ZERO.equals(fileType)){
                    if(suffix.equals(CSV) || suffix.equals(XLSX) || suffix.equals(XLS)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }else if(PublicRuleUtils.ONE.equals(fileType)){
                    if (suffix.equals(RAR) || suffix.equals(ZIP)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }else if(PublicRuleUtils.TWO.equals(fileType)){
                    if(suffix.equals(CSV) || suffix.equals(XLSX) || suffix.equals(XLS) || suffix.equals(RAR) || suffix.equals(ZIP)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }
            }
        }
        return fileList;

    }

    private void getFileList(List<FileInfoBean> fileList, File tmpFile, String suffix) {
        FileInfoBean fileInfoBean = new FileInfoBean();
        fileInfoBean.setFileName(tmpFile.getName());
        fileInfoBean.setAbsolutePath(tmpFile.getAbsolutePath());
        fileInfoBean.setFileTypeEnum(FileType_Enum.getEnumByType(suffix));
        fileList.add(fileInfoBean);
    }

    private  boolean isWindows(){
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
            return true;
        }
        return false;
    }

    private void deleteFolder(String path) {

    }


    private void createFolder(String path) {

    }
}
