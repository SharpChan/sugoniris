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
}
