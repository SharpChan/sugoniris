package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.SSHRemote.SSHConfig;
import com.sugon.iris.sugoncommon.SSHRemote.SSHServiceBs;
import com.sugon.iris.sugoncommon.fileUtils.ZipUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.JymxMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppErrorInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondata.service.DataCommonService;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.fileBeans.FileInfoBean;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessRepeatDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.MppErrorInfoEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.calculate.JymxEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugondomain.enums.FileType_Enum;
import com.sugon.iris.sugondomain.enums.Peripheral_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileDoParsingService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.declarService.DeclarService;
import com.sugon.iris.sugonservice.service.rinseBusinessService.RinseBusinessService;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
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

    private final static String DETAIL = "????????????:";

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

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileTemplateGroupMapper fileTemplateGroupMapper;

    @Resource
    private RinseBusinessService rinseBusinessServiceImpl;

    @Resource
    private FileDoParsingService fileDoParsingServiceImpl;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Resource
    private RegularDetailMapper regularDetailMapper;

    @Resource
    private JymxMapper jymxMapper;

    @Resource
    private DataCommonService dataCommonService;



    @Override
    public void test(User user) {
        String url = "http://localhost:8668/data2Mpp/test";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("userId", user.getId());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
        RestResult<Void> response = restTemplate.postForObject(url, httpEntity, RestResult.class);
    }

    //??????????????????
    @Override
    public void dataSync(User user,String[] selectedArr,List<Error> errorList) throws IOException {

    }

    @Override
    public int test(){
        Integer  list = mppMapper.mppSqlExecForSearchCount("Select fkjg,jyhm,dshm from base_bank_jymx_47_1000000001");
        log.info(list.toString());
        return 0;
    }

    //????????????????????????????????????
    @Override
    public void decompress(User user,String[] selectedArr,List<Error> errorList) throws InterruptedException, IllegalAccessException, IOException, SQLException, ExecutionException {
        log.info("????????????");
        List<FileAttachmentEntity> fileAttachmentEntityListAll = new ArrayList<>();
        for(String id : selectedArr) {
            FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
            fleAttachmentEntity.setId(Long.parseLong(id));
            fleAttachmentEntity.setUserId(user.getId());
            //?????????????????????????????????id????????????
            List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity);
            if(!CollectionUtils.isEmpty(fileAttachmentEntityList)) {
                //??????????????????????????????????????????????????????
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

        //?????????????????????????????????
        List<Long>  fileIdList = new ArrayList<>();

        //linux??????
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))) {
            SSHServiceBs  sSHServiceBs = new SSHServiceBs(session);
            try {
                if (!CollectionUtils.isEmpty(fileAttachmentEntityListAll)) {
                    for (FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll) {
                        //linux????????????
                        linuxUncompress(sSHServiceBs, fileAttachmentEntity);
                        //????????????????????????????????????????????????
                        fileIdList = readData(user, errorList, fileAttachmentEntity);
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
        //windows??????,???????????????????????????????????????????????????????????????
        else{
          try {
              if (!CollectionUtils.isEmpty(fileAttachmentEntityListAll)) {
                  for (FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll) {
                      //windows????????????
                      windowsUncompress(fileAttachmentEntity);
                      //????????????????????????????????????????????????
                      fileIdList = readData(user, errorList, fileAttachmentEntity);
                  }
              }
          }catch (Exception e){
              e.printStackTrace();
          }
        }

        //??????fileAttachmentEntityListAll????????????map???key:caseId;value:?????????id???
        Map<Long,Set<Long>> caseId2TemplateGroupIdsMap = new HashMap<>();
        Set<Long> caseIdSet = new HashSet<>();//???????????????????????????
        for(FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll){
            caseIdSet.add(fileAttachmentEntity.getCaseId());
            Set<Long> templateGroupIdList = caseId2TemplateGroupIdsMap.get(fileAttachmentEntity.getCaseId());
            if(CollectionUtils.isEmpty(templateGroupIdList)){
                Set<Long>  templateGroupIdListNew = new HashSet<>();
                templateGroupIdListNew.add(fileAttachmentEntity.getTemplateGroupId());
                caseId2TemplateGroupIdsMap.put(fileAttachmentEntity.getCaseId(),templateGroupIdListNew);
            }else{
                templateGroupIdList.add(fileAttachmentEntity.getCaseId());
            }
        }
        log.info("????????????");
        //??????????????????
        this.doFixedDefinedCompleteInCahe(user.getId(),caseId2TemplateGroupIdsMap,errorList);
        log.info("????????????");
        //????????????????????????
        log.info("??????????????????");
        this.doFixedCompleteByRemaining(caseIdSet,errorList);
        log.info("??????????????????");
        //????????????
        log.info("????????????");
        this.doRemoveRepeat(caseIdSet,errorList);
        log.info("????????????");
        //?????????????????????????????????
        log.info("?????????????????????????????????");
        this.regularCompleteField(user.getId(),fileIdList,errorList);
        log.info("?????????????????????????????????");
    }

    public void doFixedCompleteByRemaining(Set<Long> caseIdSet,List<Error>  errorList){
         for(Long caseId : caseIdSet){
               //??????caseId??????????????????
             FileTableEntity fileTableEntity4Sql = new FileTableEntity();
             fileTableEntity4Sql.setCaseId(caseId);
             List<FileTableEntity> fileTableEntityList =  fileTableMapper.findFileTableList(fileTableEntity4Sql);
             if(CollectionUtils.isEmpty(fileTableEntityList)){
                    return;
             }
             String str =  PublicUtils.getConfigMap().get("bank_jymx_id");
             if(StringUtils.isEmpty(str)){
                 str = "21";
             }
             for(FileTableEntity fileTableEntity : fileTableEntityList){
                 if(fileTableEntity.getFileTemplateId() == Long.parseLong(str.trim())){
                        String tableName = fileTableEntity.getTableName();

                        //1.?????????
                        //???????????????????????????
                        List<String> jyzhList =  jymxMapper.selectJyzh(tableName);
                        for(String jyzh : jyzhList) {

                            //???????????????????????????????????????
                            //????????????????????????
                            List<JymxEntity> jymxEntityListJyzhAll = jymxMapper.selectJymxMapperAllByJyzh(tableName,jyzh);
                            //????????????????????????????????????????????????
                            List<JymxEntity> jymxEntityListForJykh = jymxMapper.selecJymxMapperForJykh(tableName,jyzh);
                            if(CollectionUtils.isEmpty(jymxEntityListJyzhAll) || CollectionUtils.isEmpty(jymxEntityListForJykh)){
                               return;
                            }

                            //???jymxEntityListJyzhAll??????map????????????????????????
                            Map<Integer,JymxEntity> map = new HashMap<>();
                            for(JymxEntity jymxEntity : jymxEntityListJyzhAll){
                                map.put(jymxEntity.getRownum(),jymxEntity);
                            }
                            //?????????????????????????????????
                            Set<JymxEntity> jymxEntitySetForJykh = new HashSet<>(jymxEntityListForJykh);
                     start: for(JymxEntity jymxEntity : jymxEntitySetForJykh){
                                   //?????????
                               List<JymxEntity> needListForward = new ArrayList<>();
                               String jykhForward = this.forwardForJykh(jymxEntity,map,needListForward);
                               if(StringUtils.isNotBlank(jykhForward)){
                                  //????????????
                                   for(JymxEntity jymxEntityBean : needListForward) {
                                       jymxMapper.UpdateJykh(tableName, jykhForward, jymxEntityBean.getId());
                                   }
                               }else{
                                  //?????????
                                   List<JymxEntity> needListNext = new ArrayList<>();
                                   String jykhNext = this.nextForJykh(jymxEntity,map,needListNext);
                                   if(StringUtils.isNotBlank(jykhNext)){
                                       //????????????
                                       for(JymxEntity jymxEntityBean : needListNext) {
                                           jymxMapper.UpdateJykh(tableName, jykhNext, jymxEntityBean.getId());
                                       }
                                   }else{
                                       break start;
                                   }
                               }
                            }
                        }

                     //2.?????????
                     //???????????????????????????
                     List<String> jykhList =  jymxMapper.selectJykh(tableName);
                     for(String jykh : jykhList) {
                         //???????????????????????????????????????
                         //????????????????????????
                         List<JymxEntity> jymxEntityListJykhAll = jymxMapper.selectJymxMapperAllByJykh(tableName,jykh);
                         //????????????????????????????????????????????????
                         List<JymxEntity> jymxEntityListForJyzh = jymxMapper.selectJymxMapperForJyzh(tableName,jykh);
                         if(CollectionUtils.isEmpty(jymxEntityListJykhAll) || CollectionUtils.isEmpty(jymxEntityListForJyzh)){
                             return;
                         }
                         //???jymxEntityListJyzhAll??????map????????????????????????
                         Map<Integer,JymxEntity> map = new HashMap<>();
                         for(JymxEntity jymxEntity : jymxEntityListJykhAll){
                             map.put(jymxEntity.getRownum(),jymxEntity);
                         }

                         //?????????????????????????????????
                         Set<JymxEntity> jymxEntitySetForJyzh = new HashSet<>(jymxEntityListForJyzh);

                         start: for(JymxEntity jymxEntity : jymxEntitySetForJyzh){
                             //?????????
                             List<JymxEntity> needListForward = new ArrayList<>();
                             String jyzhForward = this.forwardForJyzh(jymxEntity,map,needListForward);
                             if(StringUtils.isNotBlank(jyzhForward)){
                                 //????????????
                                 for(JymxEntity jymxEntityBean : needListForward) {
                                     jymxMapper.UpdateJyzh(tableName, jyzhForward, jymxEntityBean.getId());
                                 }
                             }else{
                                 //?????????
                                 List<JymxEntity> needListNext = new ArrayList<>();
                                 String jyzhNext = this.nextForJyzh(jymxEntity,map,needListNext);
                                 if(StringUtils.isNotBlank(jyzhNext)){
                                     //????????????
                                     for(JymxEntity jymxEntityBean : needListNext) {
                                         jymxMapper.UpdateJyzh(tableName, jyzhNext, jymxEntityBean.getId());
                                     }
                                 }else{
                                     break start;
                                 }
                             }
                         }
                     }

                 }
             }
         }
    }

    //????????????,????????????,??????????????????
    private String forwardForJykh(JymxEntity jymxEntity, Map<Integer,JymxEntity> map, List<JymxEntity>  needList){
              //???????????????????????????
              needList.add(jymxEntity);
              if(null != map.get(jymxEntity.getRownum() - 1)) {
                  JymxEntity previous = map.get(jymxEntity.getRownum() - 1);
                  //????????????????????????????????????
                  if (jymxEntity.equals(map.get(jymxEntity.getRownum() - 1))) {
                      forwardForJykh(previous,map,needList);
                  }

                  //?????????????????????????????????
                  BigDecimal previousJyye = new BigDecimal(previous.getJyye());//?????????????????????
                  BigDecimal thisJyje = new BigDecimal(jymxEntity.getJyje());//??????????????????
                  BigDecimal thisJyye  = new BigDecimal(jymxEntity.getJyye());//??????????????????
                  if( previousJyye.add(thisJyje).compareTo(thisJyye) == 0){
                      if(StringUtils.isNotBlank(previous.getJykh())){
                          return previous.getJykh();
                      }else{
                          forwardForJykh(previous,map,needList);
                      }
                  }
              }
              return null;
    }

    //????????????,???????????????????????????
    private String forwardForJyzh(JymxEntity jymxEntity, Map<Integer,JymxEntity> map, List<JymxEntity>  needList){
        //???????????????????????????
        needList.add(jymxEntity);
        if(null != map.get(jymxEntity.getRownum() - 1)) {
            JymxEntity previous = map.get(jymxEntity.getRownum() - 1);
            //????????????????????????????????????
            if (jymxEntity.equals(map.get(jymxEntity.getRownum() - 1))) {
                forwardForJyzh(previous,map,needList);
            }

            //?????????????????????????????????
            BigDecimal previousJyye = new BigDecimal(previous.getJyye());//?????????????????????
            BigDecimal thisJyje = new BigDecimal(jymxEntity.getJyje());//??????????????????
            BigDecimal thisJyye  = new BigDecimal(jymxEntity.getJyye());//??????????????????
            if( previousJyye.add(thisJyje).compareTo(thisJyye) == 0){
                if(StringUtils.isNotBlank(previous.getJyzh())){
                    return previous.getJyzh();
                }else{
                    forwardForJyzh(previous,map,needList);
                }
            }
        }
        return null;
    }

    //????????????,????????????,??????????????????
    private String nextForJykh(JymxEntity jymxEntity, Map<Integer,JymxEntity> map, List<JymxEntity>  needList){
        //???????????????????????????
        needList.add(jymxEntity);
        if(null != map.get(jymxEntity.getRownum() + 1)) {
            JymxEntity next = map.get(jymxEntity.getRownum() + 1);
            //????????????????????????????????????
            if (jymxEntity.equals(map.get(jymxEntity.getRownum() + 1))) {
                nextForJykh(next,map,needList);
            }
            //?????????????????????????????????
            BigDecimal previousJyye = new BigDecimal(next.getJyye());//?????????????????????
            BigDecimal thisJyje = new BigDecimal(jymxEntity.getJyje());//??????????????????
            BigDecimal thisJyye  = new BigDecimal(jymxEntity.getJyye());//??????????????????
            if( previousJyye.add(thisJyje).compareTo(thisJyye) == 0){
                if(StringUtils.isNotBlank(next.getJykh())){
                    return next.getJykh();
                }else{
                    nextForJykh(next,map,needList);
                }
            }
        }
        return null;
    }

    //????????????,???????????????????????????
    private String nextForJyzh(JymxEntity jymxEntity, Map<Integer,JymxEntity> map, List<JymxEntity>  needList){
        //???????????????????????????
        needList.add(jymxEntity);
        if(null != map.get(jymxEntity.getRownum() + 1)) {
            JymxEntity next = map.get(jymxEntity.getRownum() + 1);
            //????????????????????????????????????
            if (jymxEntity.equals(map.get(jymxEntity.getRownum() + 1))) {
                nextForJyzh(next,map,needList);
            }
            //?????????????????????????????????
            BigDecimal previousJyye = new BigDecimal(next.getJyye());//?????????????????????
            BigDecimal thisJyje = new BigDecimal(jymxEntity.getJyje());//??????????????????
            BigDecimal thisJyye  = new BigDecimal(jymxEntity.getJyye());//??????????????????
            if( previousJyye.add(thisJyje).compareTo(thisJyye) == 0){
                if(StringUtils.isNotBlank(next.getJyzh())){
                    return next.getJyzh();
                }else{
                    nextForJyzh(next,map,needList);
                }
            }
        }
        return null;
    }


    public void regularCompleteField(Long userId,List<Long> fileIdList, List<Error> errorList) throws IllegalAccessException, IOException, SQLException {

        //?????????????????????,??????mysql
        List<FileParsingFailedEntity> fileParsingFailedEntityListSql = new ArrayList<>();

        //??????mpp
        //List<MppErrorInfoEntity> mppErrorInfoEntityList = new ArrayList<>();
        StringBuffer errorBuffer = new StringBuffer();

            //????????????id????????????
            for(Long fileId : fileIdList){
                //??????????????????
                FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileId);
                if(null == fileDetailEntity){
                    continue;
                }
                //????????????????????????
                FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(fileDetailEntity.getFileAttachmentId());
                //???????????????????????????????????????????????????
                List<FileFieldCompleteEntity> fileFieldCompleteEntityList =   fileFieldCompleteMapper.selectFileFieldCompleteByTemplateGroupId(fileAttachmentEntity.getTemplateGroupId());
                //?????????????????????????????????????????????????????????????????????????????????????????????
                Map<Long,FileTemplateEntity>  destField2Template = new HashMap<>();//??????map????????????
                for(FileFieldCompleteEntity fileFieldCompleteEntityBean : fileFieldCompleteEntityList){
                    if(fileFieldCompleteEntityBean.getDestFileTemplateId() != fileDetailEntity.getFileTemplateId()){
                         continue;
                    }
                    //????????????????????????
                    FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileFieldCompleteEntityBean.getDestFileTemplateId());

                    destField2Template.put(fileFieldCompleteEntityBean.getFieldDest(), fileTemplateEntity);
                }
                if(CollectionUtils.isEmpty(destField2Template)){
                   continue;
                }

                for(Map.Entry<Long,FileTemplateEntity> entry : destField2Template.entrySet()){
                    //????????????id?????????????????????????????????
                    FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(entry.getKey());
                    //??????????????????
                    FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailEntity.getFileRinseDetailId());
                    if(null == fileRinseDetailEntity || null == fileRinseDetailEntity.getId()){
                        continue;
                    }
                    FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
                    PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
                    //?????????????????????
                    List<RegularDetailDto>  regularDetailDtoListY = new ArrayList<>();
                    List<RegularDetailDto>  regularDetailDtoListN = new ArrayList<>();
                    //?????????????????????
                    //???????????????????????????????????????????????????
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
                    }

                    String sql = "select id from " + fileDetailEntity.getTableName() +" where file_detail_id = "+fileId ;
                    boolean flag = true;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListY)) {
                        sql +=" and (";
                        for (int i = 0; i < regularDetailDtoListY.size();i++) {
                            if(i != regularDetailDtoListY.size()-1) {
                                sql += fileTemplateDetailEntity.getFieldName() + " !~ '" + regularDetailDtoListY.get(i).getRegularValue() + "') and (";
                            }else{
                                sql += fileTemplateDetailEntity.getFieldName() + " !~ '" + regularDetailDtoListY.get(i).getRegularValue()+"'";
                            }
                        }
                        sql +=") ";
                        flag = false;
                    }

                    if(!CollectionUtils.isEmpty(regularDetailDtoListN)) {
                        sql +=" or (";
                        for (int i = 0; i < regularDetailDtoListN.size(); i++) {
                            if (i != regularDetailDtoListY.size() - 1) {
                                sql += fileTemplateDetailEntity.getFieldName() + " ~ '" + regularDetailDtoListY.get(i).getRegularValue() + "') or (";
                            } else {
                                sql += fileTemplateDetailEntity.getFieldName() + " ~ '" + regularDetailDtoListY.get(i).getRegularValue()+"'";
                            }
                        }
                        sql += ") ";
                        flag = false;
                    }

                    if(flag){
                        continue;
                    }

                    List<Long> idList =    mppMapper.mppSqlExecForSearchResInteger(sql);
                    //??????????????????
                    for(Long id : idList){
                        Long seq = mppErrorInfoMapper.selectErrorSeq();
                        FileParsingFailedEntity fileParsingFailedEntitySql = new FileParsingFailedEntity();
                        fileParsingFailedEntitySql.setRowNumber("????????????");
                        fileParsingFailedEntitySql.setFileDetailId(fileId);
                        fileParsingFailedEntitySql.setFileTemplateId(entry.getValue().getId());
                        fileParsingFailedEntitySql.setFileTemplateDetailId(entry.getKey());
                        fileParsingFailedEntitySql.setContent("????????????");
                        fileParsingFailedEntitySql.setCaseId(fileDetailEntity.getCaseId());
                        //mpp??????????????????
                        fileParsingFailedEntitySql.setTableName(fileDetailEntity.getTableName());
                        fileParsingFailedEntitySql.setMark(false);
                        fileParsingFailedEntitySql.setMppId2ErrorId(seq);
                        fileParsingFailedEntitySql.setFileAttachmentId(fileDetailEntity.getFileAttachmentId());
                        fileParsingFailedEntitySql.setUserId(userId);
                        fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);

                        MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                        mppErrorInfoEntity.setFileAttachmentId(fileDetailEntity.getFileAttachmentId());
                        mppErrorInfoEntity.setFileDetailId(fileId);
                        mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                        mppErrorInfoEntity.setMppid2errorid(seq);
                        mppErrorInfoEntity.setFileCaseId(fileDetailEntity.getCaseId());
                        mppErrorInfoEntity.setMppTableName(fileDetailEntity.getTableName());
                        Long idSeq = mppMapper.selectSeq("error_info_id_seq");
                        mppErrorInfoEntity.setId(idSeq);
                        errorBuffer.append(mppErrorInfoEntity.toString());
                    }

                }
            }
            if(!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
                fileDoParsingServiceImpl.dealWithfailed(fileParsingFailedEntityListSql,errorBuffer);
            }
    }

    //????????????
    public void doRemoveRepeat( Set<Long> caseIdSet, List<Error> errorList) throws IllegalAccessException, InterruptedException, ExecutionException {
        for (Long caseId : caseIdSet) {
            //??????????????????????????????
            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(caseId);
            List<FileTableEntity> fileTableEntityList =  fileTableMapper.findFileTableList(fileTableEntity4Sql);
            for(FileTableEntity fileTableEntityBean : fileTableEntityList){
                String repeatSql = "select c.* from (select a.*  from (select row_number() OVER(PARTITION BY _&condition&_   order by id) AS rownum,b.*   from  &&_tableName_&&  b ) a ) c where rownum > 1";

                //?????????????????????
                FileTemplateEntity fileTemplateEntity =  fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTableEntityBean.getFileTemplateId());

                //??????????????????????????????
                List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList = rinseBusinessServiceImpl.getRepetBussList(fileTemplateEntity.getId(),errorList);
                if(CollectionUtils.isEmpty(rinseBusinessRepeatDtoList)){
                    continue;
                }

                repeatSql = repeatSql.replace("&&_tableName_&&",fileTableEntityBean.getTableName());

                fileDoParsingServiceImpl.doRepeat(fileTableEntityBean.getTableName(),rinseBusinessRepeatDtoList,repeatSql);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param  caseId2TemplateGroupIdsMap  key:caseId;value:?????????id
     */
    @Override
    public void doFixedDefinedComplete(Long userId, Map<Long,Set<Long>> caseId2TemplateGroupIdsMap, List<Error> errorList) throws InterruptedException {
       try {
            for (Map.Entry<Long, Set<Long>> entry : caseId2TemplateGroupIdsMap.entrySet()) {
                Long caseId = entry.getKey();
                List<String> tableNames = getTableNames(caseId);
                Set<Long> TemplateGroupIdSet = entry.getValue();
                for (Long templateGroupId : TemplateGroupIdSet){
                    //???????????????id?????????????????????
                    List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteByTemplateGroupId(templateGroupId);
                    //????????????
                    this.fileFieldCompleteEntityListSort(fileFieldCompleteEntityList);

                    for (FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList) {
                        //????????????id
                        Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                        //????????????
                        FileTemplateEntity fileTemplateDestEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(destFileTemplateId);
                        //?????????
                        String destTable = "base_" + fileTemplateDestEntity.getTablePrefix() + "_" + caseId + "_" + userId+"_localfile";

                        if (!tableNames.contains(destTable)) {
                            continue;
                        }
                        //?????????id
                        Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                        //?????????
                        FileTemplateEntity fileTemplateSourceEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(sourceFileTemplateId);
                        String sourceTable = "base_" + fileTemplateSourceEntity.getTablePrefix() + "_" + caseId + "_" + userId+"_localfile";
                        if (!tableNames.contains(sourceTable)) {
                            continue;
                        }
                        //????????????
                        //String relations = getFieldRelation4Sql(fileFieldCompleteEntity);
                        //???????????????/????????????????????????
                        Map<String, String> source2Destmap = new HashMap<>();
                        List<String> sourceList = new ArrayList<>();
                        List<String> destList = new ArrayList<>();
                        String[] sourceFields = getDestAndSourceFields(fileFieldCompleteEntity, source2Destmap, sourceList, destList);
                        //????????????
                        String fieldSource = fileFieldCompleteEntity.getFieldSource();
                        String[] fieldSourceArr = fieldSource.split("\\+\\+");
                        List<String> fieldSourceFieldNameList = new ArrayList<>();


                        for (String str : fieldSourceArr) {
                            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str));
                            fieldSourceFieldNameList.add(fileTemplateDetailEntity.getFieldName());
                        }
                        //????????????
                        Long fieldDest = fileFieldCompleteEntity.getFieldDest();
                        FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fieldDest);
                        String fieldDestFieldName = fileTemplateDetailEntity.getFieldName();
                        //??????????????????????????????
                        for (String strSource : fieldSourceFieldNameList) {
                            String sql_01 = "select distinct " + sourceFields[3] + " from " + destTable + " where " + fieldDestFieldName + " is null ";
                            //?????????????????????   //??????????????????
                            List<Map<String, Object>> list = mppMapper.mppSqlExecForSearchRtMapList(sql_01);
                            //????????????????????????????????????????????????
                            for (Map<String, Object> map : list) {
                                if(null == map){
                                    log.info("map??????null");
                                    continue;
                                }
                                //???map??????????????????where??????
                                String whereStr = "";
                                for (Map.Entry<String, Object> entry_02 : map.entrySet()) {
                                    whereStr += entry_02.getKey() + " = '" + entry_02.getValue() + "'" + " and ";
                                }
                                whereStr = whereStr + fieldDestFieldName + " is null ";

                                //???????????????????????????   sourceList????????????????????????
                                String condi = "";
                                for (int i = 0; i < sourceList.size(); i++) {
                                    //????????????????????????,?????????????????????????????????
                                    if (sourceList.size() == 1) {
                                        if (i == sourceList.size() - 1) {
                                            condi += sourceList.get(i) + " is not null and " + sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "'";
                                        } else {
                                            condi += sourceList.get(i) + " is not null and " + sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "' and ";
                                        }
                                    }
                                    //?????????????????????
                                    else {
                                        if (i == sourceList.size() - 1) {
                                            condi += sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "'";
                                        } else {
                                            condi += sourceList.get(i) + " = '" + map.get(source2Destmap.get(sourceList.get(i))) + "' and ";
                                        }
                                    }
                                }
                                //????????????????????????????????????????????????????????????????????????
                                String sql_02 = "select count( distinct " + strSource + ") from " + sourceTable + " where " + condi + " and " + strSource + " is not null ";
                                Integer resCount = mppMapper.mppSqlExecForSearchCount(sql_02);
                                if (resCount > 1 || resCount == 0) {
                                    continue;
                                }
                                //???????????????
                                String sql_03 = "select  distinct " + strSource + " from " + sourceTable + " where " + condi + " and " + strSource + " is not null ";
                                List<Map<String, Object>> listSource = mppMapper.mppSqlExecForSearchRtMapList(sql_03);
                                //??????????????????
                                String updateSQL = "Update " + destTable + " set " + fieldDestFieldName + "= '" + listSource.get(0).get(strSource) + "' where " + whereStr;
                                mppMapper.mppSqlExec(updateSQL);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage(),e.toString()));
        }
       }

    /**
     * ????????????????????????
     *
     * @param  caseId2TemplateGroupIdsMap  key:caseId;value:?????????id
     */
    @Override
    public void doFixedDefinedCompleteInCahe(Long userId, Map<Long,Set<Long>> caseId2TemplateGroupIdsMap, List<Error> errorList) throws InterruptedException {
        try {
            for (Map.Entry<Long, Set<Long>> entry : caseId2TemplateGroupIdsMap.entrySet()) {
                Long caseId = entry.getKey();
                List<String> tableNames = getTableNames(caseId);
                Set<Long> TemplateGroupIdSet = entry.getValue();
                for (Long templateGroupId : TemplateGroupIdSet){
                    //???????????????id?????????????????????
                    List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteByTemplateGroupId(templateGroupId);
                    //????????????
                    this.fileFieldCompleteEntityListSort(fileFieldCompleteEntityList);



                    List<String> updateSqlList = new ArrayList<>();

                    for (FileFieldCompleteEntity fileFieldCompleteEntity : fileFieldCompleteEntityList) {
                        //????????????id
                        Long destFileTemplateId = fileFieldCompleteEntity.getDestFileTemplateId();
                        //????????????
                        FileTemplateEntity fileTemplateDestEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(destFileTemplateId);
                        //?????????
                        String destTable = "base_" + fileTemplateDestEntity.getTablePrefix() + "_" + caseId + "_" + userId+"_localfile";

                        if (!tableNames.contains(destTable)) {
                            continue;
                        }
                        //?????????id
                        Long sourceFileTemplateId = fileFieldCompleteEntity.getSourceFileTemplateId();
                        //?????????
                        FileTemplateEntity fileTemplateSourceEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(sourceFileTemplateId);
                        String sourceTable = "base_" + fileTemplateSourceEntity.getTablePrefix() + "_" + caseId + "_" + userId+"_localfile";
                        if (!tableNames.contains(sourceTable)) {
                            continue;
                        }
                        //????????????
                        //String relations = getFieldRelation4Sql(fileFieldCompleteEntity);
                        //???????????????/????????????????????????
                        Map<String, String> source2Destmap = new HashMap<>();
                        //????????????????????????
                        List<String> sourceRelationList = new ArrayList<>();
                        List<String> destList = new ArrayList<>();
                        String[] sourceFields = getDestAndSourceFields(fileFieldCompleteEntity, source2Destmap, sourceRelationList, destList);
                        //????????????
                        String fieldSource = fileFieldCompleteEntity.getFieldSource();
                        String[] fieldSourceArr = fieldSource.split("\\+\\+");
                        List<String> fieldSourceFieldNameList = new ArrayList<>();
                        for (String str : fieldSourceArr) {
                            FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(str));
                            fieldSourceFieldNameList.add(fileTemplateDetailEntity.getFieldName());
                        }
                        //????????????
                        Long fieldDest = fileFieldCompleteEntity.getFieldDest();
                        FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(fieldDest);
                        String fieldDestFieldName = fileTemplateDetailEntity.getFieldName();

                        //??????????????????????????????
                        for (String strSource : fieldSourceFieldNameList) {

                            //1????????????????????????
                            String condiCahe = "";
                            String mapKey = "";
                            for (int i = 0; i < sourceRelationList.size(); i++) {
                                //????????????????????????,?????????????????????????????????
                                    if (i == sourceRelationList.size() - 1) {
                                        condiCahe += sourceRelationList.get(i) + " is not null ";
                                        mapKey += sourceRelationList.get(i);
                                    } else {
                                        condiCahe += sourceRelationList.get(i) + " is not null and ";
                                        mapKey += sourceRelationList.get(i)+",";
                                    }
                            }


                            //??????????????????????????????key:???????????????value:??????
                            //Map<String,Map<String, Object>> mapCahe = new HashMap<>();
                            String sqlCahe = "select  distinct " + strSource +","+ mapKey +" from " + sourceTable + " where " + condiCahe + " and " + strSource + " is not null ";
                            List<Map<String, Object>> listForCahe = mppMapper.mppSqlExecForSearchRtMapList(sqlCahe);
                            if(CollectionUtils.isEmpty(listForCahe)){
                                continue;
                            }
                            Map<String,Object[]> mapCahe = new HashMap<>();
                            //????????????????????????????????????????????????
                            for (Map<String, Object> map : listForCahe) {

                                String key = "";
                                //???????????????????????????
                                for(String str : sourceRelationList){
                                    key += map.get(str).toString();
                                }
                                if(mapCahe.get(key) != null){
                                    mapCahe.get(key)[1] = false;
                                }else{
                                    Object[] arry = new Object[2];
                                    arry[0] = map;
                                    arry[1] = true;
                                    mapCahe.put(key,arry);
                                }
                            }

                            //??????????????????
                            if(CollectionUtils.isEmpty(mapCahe)){
                               continue;
                            }

                            //2.??????????????????????????????
                            String sql_01 = "select distinct " + sourceFields[3] + " from " + destTable + " where " + fieldDestFieldName + " is null ";
                            //?????????????????????   //??????????????????
                            List<Map<String, Object>> list = mppMapper.mppSqlExecForSearchRtMapList(sql_01);
                            for (Map<String, Object> map : list) {
                                if (null == map) {
                                    log.info("map??????null");
                                    continue;
                                }

                                //???map??????????????????where??????
                                String whereStr = "";
                                for (Map.Entry<String, Object> entry_02 : map.entrySet()) {
                                    whereStr += entry_02.getKey() + " = '" + entry_02.getValue() + "'" + " and ";
                                }
                                whereStr = whereStr + fieldDestFieldName + " is null ";

                                //???????????????key???
                                String[] arr = sourceFields[3].split(",");
                                String key = "";
                                for(String str : arr){
                                   key += map.get(str);
                                }
                                //?????????????????????
                              if( null != mapCahe.get(key) && (boolean)mapCahe.get(key)[1]){//??????????????????
                                  //??????????????????
                                  String updateSQL = "Update " + destTable + " set " + fieldDestFieldName + "= '" + ((Map)mapCahe.get(key)[0]).get(strSource) + "' where " + whereStr+";";
                                  updateSqlList.add(updateSQL);
                                  //mppMapper.mppSqlExec(updateSQL);
                              }
                            }
                        }
                    }

                    if(CollectionUtils.isEmpty(updateSqlList)){
                        continue;
                    }
                    int thread = 2;
                    if(updateSqlList.size()>10 && updateSqlList.size() < 20){
                        thread = 3;
                    }else{
                        thread = 6;
                    }
                    //?????????????????????
                    ExecutorService executorService = Executors.newFixedThreadPool(thread);
                    List<Callable<Integer>> cList = new ArrayList<>();  //???????????????????????????
                    Callable<Integer> task = null;  //??????????????????
                    for(String updateStr : updateSqlList){
                        task = new Callable<Integer>(){
                            @Override
                            public Integer call() throws Exception {
                                int i = 0;
                                try{
                                 i =   dataCommonService.mppSqlExec(updateStr);
                                }catch (Exception e){
                                    log.info("updateStr:"+updateStr);
                                    e.printStackTrace();
                                }
                              return i;
                            }
                        };
                        cList.add(task);
                    }
                    List<Future<Integer>> results = executorService.invokeAll(cList,120, TimeUnit.SECONDS); //?????????????????????????????????????????????????????????????????????????????????????????????
                    for(Future<Integer> recordPer:results){  //???????????????
                        try {
                            log.info(String.valueOf(recordPer.get()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    executorService.shutdown();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage(),e.toString()));
        }
    }

    private void mppSqlExec (String tableName,String sql){
       synchronized(tableName) {
           mppMapper.mppSqlExec(sql);
       }
   }

    private List<String> getTableNames(Long caseId) {
        //???????????????????????????Mpp??????
        FileTableEntity fileTableEntity4Sql = new FileTableEntity();
        fileTableEntity4Sql.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList =  fileTableMapper.findFileTableList(fileTableEntity4Sql);
        List<String> tableNames = new ArrayList<>();
        for(FileTableEntity fileTableEntity : fileTableEntityList){
            tableNames.add(fileTableEntity.getTableName());
        }
        return tableNames;
    }

    private List<FileParsingFailedEntity> getFileParsingFailedEntities(Long fieldDest, String mppid2errorid) {
        FileParsingFailedEntity fileParsingFailedEntity4Sql = new FileParsingFailedEntity();
        fileParsingFailedEntity4Sql.setFileTemplateDetailId(fieldDest);
        fileParsingFailedEntity4Sql.setMppId2ErrorId(Long.parseLong(mppid2errorid));
        return fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity4Sql);
    }

    //??????????????????
    private String getFieldRelation4Sql(FileFieldCompleteEntity fileFieldCompleteEntity) {
        //????????????
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        Map<Long,Long> fieldRelationMap = new HashMap<>();
        String[] fieldRelationArr = fieldRelation.split("--");
        String relations = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
            fieldRelationMap.put(Long.parseLong(fieldArr[0]),Long.parseLong(fieldArr[1]));
            //???????????????????????????
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //????????????????????????
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));

            relations += " a."+dest.getFieldName()+" = b."+source.getFieldName() + " AND ";
        }
        return relations;
    }
    //??????????????????????????????
    private String[] getSourceFields(FileFieldCompleteEntity fileFieldCompleteEntity) {
        //????????????
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        String[] fieldRelationArr = fieldRelation.split("--");
        String[] sourceFields = new String[4];
        //????????????
        String sourceFields1 = "";
        //???????????????
        String sourceFields2 = "";
        //??????????????????
        String sourceFields3 = "";

        //??????????????????
        String sourceFields4 = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
           //???????????????????????????
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //????????????????????????
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));
            sourceFields1 += " c."+source.getFieldName() + ",";
            sourceFields2 +=  source.getFieldName() + ",";
            sourceFields3 +=  source.getFieldName() + "  is not null  and ";
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
     * @param sourceList   ?????????????????????
     * @param destList
     * @return
     */
    private String[] getDestAndSourceFields(FileFieldCompleteEntity fileFieldCompleteEntity,Map<String,String> map,List<String> sourceList,List<String> destList) {
        //????????????
        String fieldRelation = fileFieldCompleteEntity.getFieldRelation();
        String[] fieldRelationArr = fieldRelation.split("--");
        String[] sourceFields = new String[4];
        //????????????
        String sourceFields1 = "";
        //???????????????
        String sourceFields2 = "";
        //??????????????????
        String sourceFields3 = "";

        //????????????????????????
        String sourceFields4 = "";
        for(String str : fieldRelationArr){
            String[] fieldArr = str.split("\\+\\+");
            //???????????????????????????
            FileTemplateDetailEntity dest = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[0]));
            //????????????????????????
            FileTemplateDetailEntity source = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[1]));
            sourceFields1 += " c."+source.getFieldName() + ",";
            sourceFields2 +=  source.getFieldName() + ",";
            sourceFields3 +=  source.getFieldName() + "  is not null  and ";
            sourceFields4 +=  dest.getFieldName()+",";
            map.put(source.getFieldName(),dest.getFieldName());
            sourceList.add(source.getFieldName());
            destList.add(dest.getFieldName());
        }
        sourceFields[0] = sourceFields1;
        sourceFields[1] = sourceFields2;
        sourceFields[2] = sourceFields3;
        sourceFields[3] = sourceFields4.substring(0,sourceFields4.lastIndexOf(","));
        return sourceFields;
    }

    @Data
    private class Complete{
        //????????????id
        private long destFileTemplateId;
        //?????????id
        private long sourceFileTemplateId;
        //????????????id
        private String fieldSource;
        //????????????id
        private long fieldDest;
        //?????????id
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
     * ?????????????????????????????????
     * @param
     */
    private static void fileFieldCompleteEntityListSort(List<FileFieldCompleteEntity> fileFieldCompleteEntityList) {
        //??????????????????????????????????????????
        Collections.sort(fileFieldCompleteEntityList, new Comparator<FileFieldCompleteEntity>() {
            @Override
            public int compare(FileFieldCompleteEntity bean1, FileFieldCompleteEntity bean2) {
                int diff = Integer.parseInt(bean1.getSortNo()) - Integer.parseInt(bean2.getSortNo());
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //?????????0
            }
        });
    }

    private List<Long> readData(User user, List<Error> errorList, FileAttachmentEntity fileAttachmentEntity) {
        //??????????????????????????????
        //????????????????????????
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
        RestResult<List<Long>> response = gson.fromJson(resultRemote, new TypeToken<RestResult<List<Long>>>(){}.getType());
        List<Long> fileIdList = response.getObj();
        if(!CollectionUtils.isEmpty(response.getErrorList())){
            errorList.addAll(response.getErrorList());
        }
        //????????????????????????
        fileAttachmentEntity.setHasImport(true);
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
        return fileIdList;
    }

    //linux???????????????
    private void linuxUncompress(SSHServiceBs sSHServiceBs, FileAttachmentEntity fileAttachmentEntity) throws IOException {
        String command = null;
        //?????????????????????
        command = "if [-d " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + "]; then rm -rf  " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " fi";
        sSHServiceBs.execCommand(command);
        if(!(".csv".equals(fileAttachmentEntity.getFileType()) || ".xls".equals(fileAttachmentEntity.getFileType())||".xlsx".equals(fileAttachmentEntity.getFileType()))) {
            command = "unar  -o " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " " + fileAttachmentEntity.getAttachment();
        }
        sSHServiceBs.execCommand(command);
        fileAttachmentEntity.setHasDecompress(true);
        //??????????????????
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
    }

    //windows???????????????
    private void windowsUncompress( FileAttachmentEntity fileAttachmentEntity) throws IOException {

        //??????????????????????????????????????????????????????
        File file = new File(fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), ""));
        deleteDir(file);
        List<String> shellList = new ArrayList<>();
        if(!(".csv".equals(fileAttachmentEntity.getFileType()) || ".xls".equals(fileAttachmentEntity.getFileType())||".xlsx".equals(fileAttachmentEntity.getFileType()))) {

            ZipUtil.unZipFiles(fileAttachmentEntity.getAttachment(),fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "\\"));
        }
        fileAttachmentEntity.setHasDecompress(true);
        //??????????????????
        fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param dir ???????????????????????????
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
           //????????????????????????????????????
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // ?????????????????????????????????
        return dir.delete();
    }

    public int uploadFile(User user, List<MultipartFile> files, Long caseId, List<Error> errorList) throws Exception {
        //??????????????????????????????????????????
        String uploadPath = null;
        //??????linux??????
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
        //??????linux??????
        if(!"1".equals(PublicUtils.getConfigMap().get("environment"))){
            session = new SSHConfig().getSession();
            sSHServiceBs = new SSHServiceBs(session);
            sSHServiceBs.listFiles(fileServerPAth,true);
            //??????????????????
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            int i = 0;
            List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
            //????????????????????????????????????????????????????????????????????????????????????
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
                //??????????????????????????????????????????
                if(user.isEconomicUser()){
                    fileAttachmentEntity.setTemplateGroupId(1L);
                    fileAttachmentEntity.setTemplateGroupName("?????????-??????");
                }
                fileAttachmentEntityList.add(fileAttachmentEntity);

                FileOutputStream fout = null;
                InputStream is = null;
                try {
                    //io ??????????????????
                    is = fileItem.getInputStream();
                    //???????????????????????????????????????
                    if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)) {
                        fout = new FileOutputStream(new File(uploadPath + "/" + fileName));
                    }else{
                        fout = new FileOutputStream(new File(uploadPath + "/" + md5FileName + type));
                    }
                    //????????????
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

                //?????????????????????????????????????????????SSHServiceBs?????????????????????????????????????????????????????????
                if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)){
                    sSHServiceBs.uploadFile(fileServerPAth+fileName,uploadPath + "/" + fileName);
                }else{
                    sSHServiceBs.uploadFile(fileServerPAth+md5FileName+type,uploadPath + "/" + md5FileName+type);
                }
                sSHServiceBs.closeSession(session);
                //??????????????????
                File file = new File(uploadPath + "/" + md5FileName+type);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                i++;
            }
            //?????????????????????
            fileAttachmentMapper.batchFileAttachmentInsert(fileAttachmentEntityList);
            return i;
        }
        //??????windows??????
        else{
            //??????????????????
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            int i = 0;
            List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
            //????????????????????????????????????????????????????????????????????????????????????
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
                //??????????????????????????????????????????
                if(user.isEconomicUser()){
                    fileAttachmentEntity.setTemplateGroupId(1L);
                    //??????????????????????????????????????????
                    FileTemplateGroupEntity fileTemplateGroupEntity = new FileTemplateGroupEntity();
                    fileTemplateGroupEntity.setGroupId(1L);
                    List<FileTemplateGroupEntity> fileTemplateGroupEntityList =  fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntity);
                    String groupName = "";
                    if(!CollectionUtils.isEmpty(fileTemplateGroupEntityList)) {
                        groupName = fileTemplateGroupEntityList.get(0).getGroupName();
                    }
                    fileAttachmentEntity.setTemplateGroupName(groupName);
                }
                //??????????????????????????????????????????
                if(user.isEconomicUser()){
                    fileAttachmentEntity.setTemplateGroupId(1L);
                }
                fileAttachmentEntityList.add(fileAttachmentEntity);

                FileOutputStream fout = null;
                InputStream is = null;
                try {
                    //io ??????????????????
                    is = fileItem.getInputStream();
                    //???????????????????????????????????????
                    if(".csv".equals(type) || ".xls".equals(type)||".xlsx".equals(type)) {
                        fout = new FileOutputStream(new File(uploadPath + "/" + fileName));
                    }else{
                        fout = new FileOutputStream(new File(uploadPath + "/" + md5FileName + type));
                    }
                    //????????????
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
            //?????????????????????
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
                    //???????????????????????????????????????????????????
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
                        //???????????????????????????
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
                    errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????declaration??????",e.toString()));
                }

                if (arrList.size() == 0) {
                    return j;
                }
            }
        }
        arr  = (String[])arrList.toArray(new String[arrList.size()]);

        int i = 0;
        for(String id :arr ){
            //???????????????????????????????????????????????????
            FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(Long.parseLong(id));
            if(null == fileAttachmentEntity){
              return  j;
            }
            Session session = new SSHConfig().getSession();
            SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
            sSHServiceBs = new SSHServiceBs(new SSHConfig().getSession());
            //??????????????????
            //sSHServiceBs.deleteFile(fileAttachmentEntity.getAttachment());

            //??????????????????
            String path = null;
            if(fileAttachmentEntity.getAttachment().contains(".")) {
                 path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf("."));
            }else if(fileAttachmentEntity.getAttachment().contains("/")){
                path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf(""));
            }
            String command = "rm -rf  " + path;
            sSHServiceBs.execCommand(command);
            //??????session
            sSHServiceBs.closeSession(session);
            //??????mpp??????????????????
            //??????fleAttachmentEntity???id????????????????????????
            FileDetailEntity fileDetailEntitySql = new FileDetailEntity();
            fileDetailEntitySql.setFileAttachmentId(fileAttachmentEntity.getId());
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntitySql);
            //??????mpp??????
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                if(StringUtils.isEmpty(fileDetailEntityBean.getTableName())){
                    continue;
                }
                //?????????????????????
                String sql = "delete from  "+fileDetailEntityBean.getTableName()+" where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
                mppMapper.mppSqlExec(sql);
                //?????????????????????
                String originSql = "delete from  "+fileDetailEntityBean.getOriginTableName()+" where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
                mppMapper.mppSqlExec(originSql);
            }
            //??????????????????
            fileDetailMapper.deleteFileDetailByFileAttachmentId(fileAttachmentEntity.getId());
            fileAttachmentMapper.deleteFileAttachmentById(fileAttachmentEntity);
            //???????????????????????????
            fileParsingFailedMapper.deleteFileParsingFailedByFileAttachmentId(fileAttachmentEntity.getId());
            //??????mpp????????????error_info??????
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
            //??????????????????
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
     * ???????????????
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
     * @param file:???????????????
     * @param fileType??????????????????????????? 0??????????????????1????????????csv???Excel???Excels???2???????????????
     */
    private List<FileInfoBean> getFileNameAndPath(File file , String fileType) {
        //????????????????????????????????????
        List<FileInfoBean> fileList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File tmpFile :files){
            if (tmpFile.isDirectory()){//?????????????????? ????????????
                getFileNameAndPath(tmpFile,fileType);
            }else {//???????????????
                //??????????????????.?????????
                int lastIndexOf = tmpFile.getAbsolutePath().lastIndexOf(".");
                //????????????????????????
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
