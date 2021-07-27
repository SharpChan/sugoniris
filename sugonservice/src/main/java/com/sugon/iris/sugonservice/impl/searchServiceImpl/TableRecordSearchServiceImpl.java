package com.sugon.iris.sugonservice.impl.searchServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.searchDtos.TableRecordSearchDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.searchService.TableRecordSearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableRecordSearchServiceImpl implements TableRecordSearchService {
    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Override
    public List<TableRecordSearchDto> getRecordsByUserId(Long userId, String condition,List<Error> errorList) throws IllegalAccessException {
        if(StringUtils.isEmpty(condition)){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_005.getCode(),ErrorCode_Enum.SUGON_01_005.getMessage()));
            return null;
        }
        //1.对查询条件进行校验，排除关键字
        String str = PublicUtils.checkSql(condition);
        //1.查询出拥有权限的数据表
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findAllFileTablesByUserId(userId);
        if(CollectionUtils.isEmpty(fileTableEntityList)){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_006.getCode(),ErrorCode_Enum.SUGON_01_006.getMessage()));
            return null;
        }
        List<TableRecordSearchDto> tableRecordSearchDtoList = new ArrayList<>();
        //2.查询出,模板
        List<FileTemplateDto>  fileTemplateDtoList = new ArrayList<>();
        for(FileTableEntity ｆileTableEntity : fileTableEntityList){
            FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
            fileTemplateEntity.setId(ｆileTableEntity.getFileTemplateId());
            FileTemplateDto FileTemplateDto = new FileTemplateDto();
            fileTemplateDtoList.add( PublicUtils.trans(fileTemplateMapper.selectFileTemplateList(fileTemplateEntity).get(0),FileTemplateDto));
        }

        //2.1查询模板内字段

        for(FileTemplateDto FileTemplateDto : fileTemplateDtoList){
            FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
            fileTemplateDetailEntity.setTemplateId(FileTemplateDto.getId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity);
            for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList){
                FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
                FileTemplateDto.getFileTemplateDetailDtoList().add(PublicUtils.trans(fileTemplateDetailEntityBean,fileTemplateDetailDto));
            }
        }

        //3.组装sql
        //3.1 组装查询条件
        String conditionNew = this.doCondition(condition);

        //3.2
        String sqlStr = "";
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            if(!CollectionUtils.isEmpty(fileTemplateDto.getFileTemplateDetailDtoList())){
                String  unitStr = "select result from (select  concat(";
                //获取表名
                for(FileTableEntity fileTableEntity : fileTableEntityList){
                     if (fileTemplateDto.getId().equals(fileTableEntity.getFileTemplateId())){
                         int k = 0;
                         for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                             k++;
                             unitStr +="'||" +fileTemplateDetailDto.getFieldKey()+":',"+fileTemplateDetailDto.getFieldName()+",'||'";
                             if(k != fileTemplateDto.getFileTemplateDetailDtoList().size()){
                                 unitStr +=",";
                             }
                         }
                         unitStr +=") as result from "+fileTableEntity.getTableName()+") a  where 1=1  "+conditionNew;
                         //进行查询
                        List<String>  resultList = mppMapper.mppSqlExecForSearch(unitStr);
                        if(CollectionUtils.isEmpty(resultList)){
                             continue;
                        }
                        //通过caseId获取案件信息
                         FileCaseEntity fileCaseEntity = new FileCaseEntity();
                         fileCaseEntity.setId(fileTableEntity.getCaseId());
                         List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity);

                        TableRecordSearchDto tableRecordSearchDto = new TableRecordSearchDto();
                        tableRecordSearchDto.setCaseName(fileCaseEntityList.get(0).getCaseName());
                        tableRecordSearchDto.setCaseNo(fileCaseEntityList.get(0).getCaseNo());
                        tableRecordSearchDto.setTemplateName(fileTemplateDto.getTemplateName());
                        tableRecordSearchDto.setResult(resultList);
                        tableRecordSearchDto.setTableName(fileTableEntity.getTableName());

                        tableRecordSearchDtoList.add(tableRecordSearchDto);
                        break;
                     }
                }
            }
        }
        return tableRecordSearchDtoList;
    }

    private String doCondition(String condition){
        //4.对查询条件进行解析，&&条件与||条件或，||（或）的优先级大于&&（与）
        String conditionNew ="";
        //用&&进行分割
        if(condition.contains("&&")){
            String[] conditionArr1 = condition.split("&&");
            for(String conditionStr1 : conditionArr1){
                if(conditionStr1.contains("||")){
                    String[] conditionArr2 = conditionStr1.split("||");
                    conditionNew += "and(";
                    int k = 0;
                    for(String conditionStr2 : conditionArr2){
                        k++;
                        conditionNew += "result like '%"+conditionStr2+"%' ";
                        if(k != conditionArr2.length){
                            conditionNew += " or ";
                        }
                    }
                    conditionNew +=")";
                }else{
                    conditionNew += "and result like '%"+conditionStr1+"%'";
                }
            }
        }else if(condition.contains("||")){

            String[] conditionArr3 = condition.split("&&");
            conditionNew += " and (";
            int h =0;
            for(String conditionStr3 : conditionArr3){
                h++;
                conditionNew += "result like '%"+conditionStr3+"%'";
                if(h!= conditionArr3.length){
                    conditionNew += "or ";
                }
                conditionNew +=")";
            }

        }else if(StringUtils.isNotEmpty(condition)){
            conditionNew += "and result like '%"+condition+"%'";
        }
        return conditionNew;
    }
}
