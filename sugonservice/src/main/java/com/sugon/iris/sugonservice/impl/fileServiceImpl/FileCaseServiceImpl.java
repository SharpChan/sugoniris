package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.DeclarMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileAttachmentMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarationDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
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

    @Override
    public Integer saveCase(User user, FileCaseDto fileCaseDto, List<Error> errorList) throws IllegalAccessException {
        int i =0;
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        PublicUtils.trans(fileCaseDto,fileCaseEntity);
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
    public Integer deleteCase(User user,String[] arr,boolean flag,List<Error> errorList) throws IllegalAccessException {
        //对超过一定时间的案件删除，走申报流程
        List<String> arrList = new ArrayList<>(Arrays.asList(arr));
        int j = 0;
        if(flag && StringUtils.isNotEmpty(PublicUtils.getConfigMap().get("case_delete_time"))) {
            String time = PublicUtils.getConfigMap().get("case_delete_time").trim().replaceAll("^\\s*$", "");
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            if (StringUtils.isNotEmpty(time) && pattern.matcher(time).matches()) {
                List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
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
                if(!CollectionUtils.isEmpty(declarationDetailDtoList)) {
                    declarServiceImpl.saveDeclaration(user, declarationDetailDtoList, errorList);
                }
                if (arrList.size() == 0) {
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
            fileTableMapper.deleteFileTableByCaseId(arr);
            count =  fileCaseMapper.deleteFileCaseById(arr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_case出错",e.toString()));
        }
        return count+j;
    }
}
