package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDataGroupTableMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupTableEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.FileService.FileDataGroupTableService;
import com.sugon.iris.sugondomain.beans.system.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDataGroupTableServiceImpl implements FileDataGroupTableService {

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileDataGroupTableMapper fileDataGroupTableMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Override
    public List<MenuDto> findDataGroupTableByUserId(User user, Long dataGroupId, List<Error> errorList) throws IllegalAccessException {
        //1.获取数据组下的表
        FileDataGroupTableEntity fileUserTableEntitySqlParm  = new FileDataGroupTableEntity();
        fileUserTableEntitySqlParm.setCreateUserId(user.getId());
        fileUserTableEntitySqlParm.setDataGroupId(dataGroupId);
        List<FileDataGroupTableEntity> fileUserTableEntityList = fileDataGroupTableMapper.findFileDataGroupTable(fileUserTableEntitySqlParm);

        //2.获取该用户的所有的数据表
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
                    menuDtoTable.setName(fileTableEntityBean.getTableName());
                    menuDtoTable.setId(fileTableEntityBean.getId());
                    for(FileDataGroupTableEntity fileDataGroupTableEntityBean : fileUserTableEntityList){
                        if(fileDataGroupTableEntityBean.getTableId().equals(fileTableEntityBean.getId())){
                            menuDtoTable.setIsChecked(true);
                        }
                    }
                }
            }
        }
        return menuDtoList;
    }

    @Override
    public Integer removeFileDataGroupTables(User user, List<OwnerMenuDto> fileDataGroupTableList, List<Error> errorList) {
        List<Long> idList = new ArrayList<>();
        for(OwnerMenuDto ownerMenuDto : fileDataGroupTableList){
            if(null == ownerMenuDto.getMenuId()){
                continue;
            }
            FileDataGroupTableEntity fileDataGroupTableEntitySqlParm = new FileDataGroupTableEntity();
            fileDataGroupTableEntitySqlParm.setTableId(ownerMenuDto.getMenuId());
            fileDataGroupTableEntitySqlParm.setDataGroupId(ownerMenuDto.getOwnerId());
            fileDataGroupTableEntitySqlParm.setCreateUserId(user.getId());
            idList.add(fileDataGroupTableMapper.findFileDataGroupTable(fileDataGroupTableEntitySqlParm).get(0).getId());
        }
        return fileDataGroupTableMapper.deleteFileDataGroupTables(idList);
    }

    @Override
    public Integer saveFileDataGroupTables(User user, List<OwnerMenuDto> fileDataGroupTableList, List<Error> errorList) {
        List<FileDataGroupTableEntity> fileDataGroupTableEntitySqlParmList = new ArrayList<>();
        for(OwnerMenuDto ownerMenuDto : fileDataGroupTableList){
            if(null == ownerMenuDto.getMenuId()){
                continue;
            }
            FileDataGroupTableEntity fileDataGroupTableEntity = new FileDataGroupTableEntity();
            fileDataGroupTableEntity.setCreateUserId(user.getId());
            fileDataGroupTableEntity.setDataGroupId(ownerMenuDto.getOwnerId());
            fileDataGroupTableEntity.setTableId(ownerMenuDto.getMenuId());
            fileDataGroupTableEntitySqlParmList.add(fileDataGroupTableEntity);
        }

        try {
            fileDataGroupTableMapper.saveFileDataGroupTables(fileDataGroupTableEntitySqlParmList);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_data_group_table出错",e.toString()));
        }
        return null;
    }
}
