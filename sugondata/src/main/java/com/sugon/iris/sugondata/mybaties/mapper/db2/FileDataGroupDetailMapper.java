package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.UserEntity;
import java.util.List;

public interface FileDataGroupDetailMapper {

    List<UserEntity> findFileDataGroupUsersByGroupId(Long groupId);

    List<UserEntity> findUsersNotInDataGroupsByUserId(Long groupId);

    Integer saveUserFromDataGroupDetail(List<FileDataGroupDetailEntity> fileDataGroupDetailEntityList);

    Integer deleteFileDataGroupDetail(FileDataGroupDetailEntity fileDataGroupDetailEntity);

}
