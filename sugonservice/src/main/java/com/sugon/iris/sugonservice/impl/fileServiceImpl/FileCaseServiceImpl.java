package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarationDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileOriginTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.declarService.DeclarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FileCaseServiceImpl implements FileCaseService {

    private final static String DETAIL = "案件删除:";

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private DeclarService declarServiceImpl;

    @Resource
    private DeclarMapper declarMapper;

    @Resource
    private FileOriginTableMapper fileOriginTableMapper;

    @Resource
    private MppMapper mppMapper;

    @Override
    public Integer saveCase(User user, FileCaseDto fileCaseDto, List<Error> errorList) throws IllegalAccessException {
        int i =0;
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        PublicUtils.trans(fileCaseDto,fileCaseEntity);

        //查看案件编号和案件名称有没有重复
        FileCaseEntity fileCaseEntity4Sql_1 = new FileCaseEntity();
        fileCaseEntity4Sql_1.setCaseName(fileCaseDto.getCaseName());
        List<FileCaseEntity> fileCaseEntityListCaseName =  fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql_1);
        if(!CollectionUtils.isEmpty(fileCaseEntityListCaseName)){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_012.getCode(),"案件名称重复"));
            return i;
        }

        FileCaseEntity fileCaseEntity4Sql_2 = new FileCaseEntity();
        fileCaseEntity4Sql_2.setCaseNo(fileCaseDto.getCaseNo());
        List<FileCaseEntity> fileCaseEntityListCaseNo =  fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql_2);
        if(!CollectionUtils.isEmpty(fileCaseEntityListCaseNo)){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_012.getCode(),"案件编号重复"));
            return i;
        }

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

        //查看修改的编号和案件名称有没有重复
        List<FileCaseEntity> fileCaseEntitySetList = new ArrayList<>();
        //通过案件编号查询
        FileCaseEntity fileCaseEntity4Sql_1 = new FileCaseEntity();
        fileCaseEntity4Sql_1.setCaseNo(fileCaseDto.getCaseNo());
        List<FileCaseEntity> fileCaseEntityListByCaseNo =  fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql_1);
        if(!CollectionUtils.isEmpty(fileCaseEntityListByCaseNo)){
            fileCaseEntitySetList.addAll(fileCaseEntityListByCaseNo);
        }

        FileCaseEntity fileCaseEntity4Sql_2 = new FileCaseEntity();
        fileCaseEntity4Sql_2.setCaseName(fileCaseDto.getCaseName());
        List<FileCaseEntity> fileCaseEntityListByCaseName =  fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql_2);
        if(!CollectionUtils.isEmpty(fileCaseEntityListByCaseName)){
            fileCaseEntitySetList.addAll(fileCaseEntityListByCaseName);
        }

        if(!CollectionUtils.isEmpty(fileCaseEntitySetList)){
            for(FileCaseEntity fileCaseEntityBean : fileCaseEntitySetList){
               if(fileCaseEntityBean.getId().equals(fileCaseDto.getId())){
                    continue;
               }
               if(fileCaseEntityBean.getCaseName().equals(fileCaseDto.getCaseName())){
                   errorList.add(new Error(ErrorCode_Enum.SUGON_01_012.getCode(),"案件名称重复"));
                   return i;
               }
                if(fileCaseEntityBean.getCaseNo().equals(fileCaseDto.getCaseNo())){
                    errorList.add(new Error(ErrorCode_Enum.SUGON_01_012.getCode(),"案件编号重复"));
                    return i;
                }
            }
        }

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

                //查询申报状态
                DeclarationDetailEntity declarationDetailEntitySql = new DeclarationDetailEntity();
                declarationDetailEntitySql.setBusinessType(PublicRuleUtils.ONE);
                declarationDetailEntitySql.setBusinessId(fileCaseEntityBean.getId());
                List<DeclarationDetailEntity> declarationDetailEntityList = declarMapper.findDeclarationDetail(declarationDetailEntitySql);
                if(CollectionUtils.isEmpty(declarationDetailEntityList)){
                    fileCaseDtoBean.setDeclarationStatus(PublicRuleUtils.ZERO);
                }else if(PublicRuleUtils.ZERO.equals(declarationDetailEntityList.get(0).getStatus())){
                    fileCaseDtoBean.setDeclarationStatus(PublicRuleUtils.ONE);
                }else if(PublicRuleUtils.TWO.equals(declarationDetailEntityList.get(0).getStatus())){
                    fileCaseDtoBean.setDeclarationStatus(PublicRuleUtils.TWO);
                }
                fileCaseDtoList.add(PublicUtils.trans(fileCaseEntityBean,fileCaseDtoBean));
            }
        }
        return fileCaseDtoList;
    }

    @Override
    public List<FileCaseDto> selectFileCaseEntityListHasTable(Long userId, List<Error> errorList) throws IllegalAccessException {
        List<FileCaseDto> fileCaseDtoList = new ArrayList<>();
        List<FileCaseEntity> fileCaseEntityList =  fileCaseMapper.selectFileCaseEntityListHasTable(userId);
        for(FileCaseEntity fileCaseEntity : fileCaseEntityList){
            FileCaseDto fileCaseDto = new FileCaseDto();
            PublicUtils.trans(fileCaseEntity , fileCaseDto);
            fileCaseDtoList.add(fileCaseDto);
        }
        return fileCaseDtoList;
    }



    @Override
    public Integer deleteCase(User user,String[] arr,boolean flag,List<Error> errorList) throws IllegalAccessException {
        //对超过一定时间的案件删除，走申报流程
        List<String> arrList = new ArrayList<>(Arrays.asList(arr));
        int j = 0;
        List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
        if(flag && StringUtils.isNotEmpty(PublicUtils.getConfigMap().get("case_delete_time"))) {
            String time = PublicUtils.getConfigMap().get("case_delete_time").trim().replaceAll("^\\s*$", "");
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            if (StringUtils.isNotEmpty(time) && pattern.matcher(time).matches()) {

                for (Iterator<String> it = arrList.iterator(); it.hasNext();) {
                    String   id = it.next();
                    FileCaseEntity fileCaseEntitySql = new FileCaseEntity();
                    fileCaseEntitySql.setId(Long.parseLong(id));
                    FileCaseEntity fileCaseEntity = fileCaseMapper.selectFileCaseEntityList(fileCaseEntitySql).get(0);
                    Long createTime = fileCaseEntity.getCreateTime().getTime();
                    Long nowTime = new Date().getTime();

                    if ((nowTime - createTime) / 1000 / 60 / 60 > Long.parseLong(time)) {
                        DeclarationDetailDto declarationDetailDto = new DeclarationDetailDto();
                        declarationDetailDto.setDetail(DETAIL + fileCaseEntity.getCaseName());
                        declarationDetailDto.setOwnerUserId(user.getId());
                        declarationDetailDto.setStatus(PublicRuleUtils.ZERO);
                        declarationDetailDto.setBusinessType(PublicRuleUtils.ONE);
                        declarationDetailDto.setBusinessId(fileCaseEntity.getId());
                        declarationDetailDtoList.add(declarationDetailDto);
                        //删除数组中对应的值
                        it.remove();
                        j++;
                    }
                }

                if (arrList.size() == 0) {
                    if(!CollectionUtils.isEmpty(declarationDetailDtoList)){
                        declarServiceImpl.saveDeclaration(user, declarationDetailDtoList, errorList);
                    }
                    return j;
                }
            }
        }
        arr  = (String[])arrList.toArray(new String[arrList.size()]);

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
                folderServiceImpl.deleteFile(user, (String[]) fileAttachmentIdList.toArray(new String[fileAttachmentIdList.size()]),false,errorList);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除文件和对应的信息出错",e.toString()));
        }
        try{
            //通过caseId案件编号，删除mpp内表
            for(String caseId : arr){
                //查询原始表表名,并删除。
                FileOriginTableEntity fileOriginTableEntity4Sql = new FileOriginTableEntity();
                fileOriginTableEntity4Sql.setCaseId(Long.parseLong(caseId));
                List<FileOriginTableEntity> fileOriginTableEntityList = fileOriginTableMapper.findFileOriginTableList(fileOriginTableEntity4Sql);
                for(FileOriginTableEntity fileOriginTableEntity : fileOriginTableEntityList) {
                    String sql = "DROP TABLE " +fileOriginTableEntity.getTableName();
                    try {
                        mppMapper.mppSqlExec(sql);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //查询资源表名并删除
                FileTableEntity fileTableEntity4Sql = new FileTableEntity();
                fileTableEntity4Sql.setCaseId(Long.parseLong(caseId));
                List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity4Sql);
                for(FileTableEntity fileTableEntity : fileTableEntityList){
                    String sql = "DROP TABLE " +fileTableEntity.getTableName();
                    mppMapper.mppSqlExec(sql);
                }
            }
            //删除表信息
            fileTableMapper.deleteFileTableByCaseId(arr);
            //删除原始数据表信息
            fileOriginTableMapper.deleteFileOriginTableByCaseId(arr);
            //删除案件信息
            count =  fileCaseMapper.deleteFileCaseById(arr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_case出错",e.toString()));
        }
        try{
            if(!CollectionUtils.isEmpty(declarationDetailDtoList)) {
                declarServiceImpl.saveDeclaration(user, declarationDetailDtoList, errorList);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表declaration出错",e.toString()));
        }
        return count+j;
    }
}
