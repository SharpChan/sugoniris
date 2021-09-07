package com.sugon.iris.sugonservice.impl.neo4jServiceImpl;

import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDataGroupTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.Neo4jNodeAttributeMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jNodeAttributeEntity;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jRelationService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class Neo4jRelationServiceImpl implements Neo4jRelationService {

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileDataGroupTableMapper fileDataGroupTableMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private Neo4jNodeAttributeMapper neo4jNodeAttributeMapper;


    @Override
    public List<MenuDto> findNeo4jNodeAttributeByUserId(User user, List<Error> errorList) throws IllegalAccessException {
        //获取该用户的所有的数据表
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setUserId(user.getId());
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //3.获取该用户下所有的案件
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        fileCaseEntity.setUserId(user.getId());
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity);
        List<MenuDto> menuDtoList = new ArrayList<>();
        for(FileCaseEntity fileCaseEntityBean : fileCaseEntityList){
            MenuDto menuDtoCase = new MenuDto();
            menuDtoList.add(menuDtoCase);
            menuDtoCase.setName(fileCaseEntityBean.getCaseName());
            List<MenuDto> submenu = new ArrayList<>();
            menuDtoCase.setSubmenu(submenu);
            for(FileTableEntity fileTableEntityBean : fileTableEntityList){
                if(fileTableEntityBean.getCaseId().equals(fileCaseEntityBean.getId())){
                    MenuDto menuDtoTable = new MenuDto();
                    submenu.add(menuDtoTable);
                    menuDtoTable.setName(fileTableEntityBean.getTableName()+"["+fileTableEntityBean.getTitle().substring(fileTableEntityBean.getTitle().indexOf("_")+1)+"]");
                    menuDtoTable.setId(fileTableEntityBean.getId());
                    List<MenuDto> submenuAttributeList = new ArrayList<>();
                    menuDtoTable.setSubmenu(submenuAttributeList);
                    Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql = new Neo4jNodeAttributeEntity();
                    neo4jNodeAttributeEntity4Sql.setFileTableId(fileTableEntityBean.getId());
                    List<Neo4jNodeAttributeEntity> neo4jNodeAttributeEntityList = neo4jNodeAttributeMapper.getNeo4jNodeAttributeLis(neo4jNodeAttributeEntity4Sql);
                    for(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity : neo4jNodeAttributeEntityList){
                        MenuDto menuAttributeDto = new MenuDto();
                        menuAttributeDto.setName(neo4jNodeAttributeEntity.getAttributeName());
                        menuAttributeDto.setId(neo4jNodeAttributeEntity.getId());
                        submenuAttributeList.add(menuAttributeDto);
                    }
;                }
            }
        }
        return menuDtoList;
    }
}
