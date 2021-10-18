package com.sugon.iris.sugonservice.impl.declarServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.DeclarMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarInfoDto;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarInfoBeanEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarationDetailEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.declarService.DeclarService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeclarServiceImpl implements DeclarService {

    @Resource
    private DeclarMapper declarMapper;

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Override
    public List<DeclarInfoDto> getDeclarInfo(Long userId, List<Error> errorList) throws IllegalAccessException {
        List<DeclarInfoDto> declarInfoDtoList = new ArrayList<>();
        List<DeclarInfoBeanEntity> declarInfoBeanEntityList = null;
        try {
             declarInfoBeanEntityList = declarMapper.getDeclarInfoByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表declaration出错",e.toString()));
        }
        if(!CollectionUtils.isEmpty(declarInfoBeanEntityList)){
            for(DeclarInfoBeanEntity declarInfoBeanEntityBean : declarInfoBeanEntityList){
                DeclarInfoDto declarInfoDto = new DeclarInfoDto();
                if(PublicRuleUtils.ZERO.equals(declarInfoBeanEntityBean.getStatus())){
                    declarInfoDto.setDeclarName("待审核");
                }else if(PublicRuleUtils.ONE.equals(declarInfoBeanEntityBean.getStatus())){
                    declarInfoDto.setDeclarName("审核通过");
                }else if(PublicRuleUtils.TWO.equals(declarInfoBeanEntityBean.getStatus())){
                    declarInfoDto.setDeclarName("审核不通过");
                }
                declarInfoDtoList.add(PublicUtils.trans(declarInfoBeanEntityBean, declarInfoDto));
            }
        }
        return declarInfoDtoList;
    }

    @Override
    public List<DeclarationDetailDto> getDeclarDetail(Long userId, String status, List<Error> errorList) throws IllegalAccessException {
        DeclarationDetailEntity declarationDetailEntitySql = new DeclarationDetailEntity();
        declarationDetailEntitySql.setStatus(status);
        declarationDetailEntitySql.setOwnerUserId(userId);
        List<DeclarationDetailEntity> declarationDetailEntityList = null;
        try {
            declarationDetailEntityList = declarMapper.findDeclarationDetail(declarationDetailEntitySql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表declaration出错",e.toString()));
        }
        List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
        for(DeclarationDetailEntity declarationDetailEntity : declarationDetailEntityList){
            DeclarationDetailDto declarationDetailDto = new DeclarationDetailDto();
           if( PublicRuleUtils.ONE.equals(declarationDetailEntity.getType()) ) {
               declarationDetailDto.setTypeName("删除案件");
           }else if( PublicRuleUtils.TWO.equals(declarationDetailEntity.getType()) ){
               declarationDetailDto.setTypeName("删除文件");
           }
            declarationDetailDtoList.add(PublicUtils.trans(declarationDetailEntity,declarationDetailDto));

        }
        return declarationDetailDtoList;
    }

    @Override
    public List<DeclarationDetailDto> getAllDeclarDetail(DeclarationDetailDto declarationDetail,List<Error> errorList) throws IllegalAccessException {
        List<DeclarationDetailEntity> declarationDetailEntityList = null;
        DeclarationDetailEntity declarationDetailEntitySql = new DeclarationDetailEntity();
        declarationDetailEntitySql.setType(declarationDetail.getType());
        declarationDetailEntitySql.setStatus(declarationDetail.getStatus());
        try {
            declarationDetailEntityList = declarMapper.findDeclarationDetail4Check(declarationDetailEntitySql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表declaration出错",e.toString()));
        }
        List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
        for(DeclarationDetailEntity declarationDetailEntity : declarationDetailEntityList){
            DeclarationDetailDto declarationDetailDto = new DeclarationDetailDto();
            if( PublicRuleUtils.ONE.equals(declarationDetailEntity.getType()) ) {
                declarationDetailDto.setTypeName("删除案件");
            }else if( PublicRuleUtils.TWO.equals(declarationDetailEntity.getType()) ){
                declarationDetailDto.setTypeName("删除文件");
            }

            if( PublicRuleUtils.ZERO.equals(declarationDetailEntity.getStatus()) ) {
                declarationDetailDto.setStatusName("未审核");;
            }else if( PublicRuleUtils.ONE.equals(declarationDetailEntity.getStatus()) ){
                declarationDetailDto.setStatusName("审核通过");
            }else if( PublicRuleUtils.TWO.equals(declarationDetailEntity.getStatus()) ){
                declarationDetailDto.setStatusName("审核不通过");
            }
            declarationDetailDtoList.add(PublicUtils.trans(declarationDetailEntity,declarationDetailDto));

        }
        return declarationDetailDtoList;
    }

    @Override
    public int saveDeclaration(User user, List<DeclarationDetailDto> declarationDetailDtoList, List<Error> errorList) throws IllegalAccessException {
        List<DeclarationDetailEntity> declarationDetailEntityList = new ArrayList<>();
        for(DeclarationDetailDto declarationDetailDto : declarationDetailDtoList){
            DeclarationDetailEntity declarationDetailEntity = new DeclarationDetailEntity();
            PublicUtils.trans(declarationDetailDto,declarationDetailEntity);
            declarationDetailEntity.setOwnerUserId(user.getId());
            declarationDetailEntityList.add(declarationDetailEntity);
        }
        return declarMapper.saveDeclarationDetail(declarationDetailEntityList);
    }

    @Override
    public int deleteDeclarDetail(String[] arr, List<Error> errorList) {
        int i=0;
        try {
            i = declarMapper.deleteDeclarationDetail(arr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表declaration出错",e.toString()));
        }
        return i;
    }

    @Override
    public int failedDeclar(Long userId,String[] arr, List<Error> errorList) {
        int i=0;
        try {
            i = declarMapper.updateDeclaration(userId, "2", arr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表declaration出错",e.toString()));
        }
        return   i;
    }

    @Override
    public int approveDeclar(Long userId, String[] arr, List<Error> errorList) throws Exception {
        int i = 0 ;
        //通过id获取申报信息
        for(String id : arr){
            DeclarationDetailEntity declarationDetailEntity = new DeclarationDetailEntity();
            declarationDetailEntity.setId(Long.parseLong(id));
            declarationDetailEntity = declarMapper.findDeclarationDetail(declarationDetailEntity).get(0);
            String[] idArr = {declarationDetailEntity.getBusinessId()+""};
            //案件删除
            if(PublicRuleUtils.ONE.equals(declarationDetailEntity.getType())){
                User user = new User();
                user.setId(declarationDetailEntity.getOwnerUserId());
                fileCaseServiceImpl.deleteCase(user,idArr,false,errorList);
            }
            //文件删除
            if(PublicRuleUtils.TWO.equals(declarationDetailEntity.getType())){
                User user = new User();
                user.setId(declarationDetailEntity.getOwnerUserId());
                folderServiceImpl.deleteFile(user,idArr,false,errorList);
            }

            declarMapper.updateDeclaration(userId, "1", arr);
            i++;
        }
        return i;
    }
}
