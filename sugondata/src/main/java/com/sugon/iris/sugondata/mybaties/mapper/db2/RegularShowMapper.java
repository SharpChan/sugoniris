package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularShowEntity;
import java.util.List;

public interface RegularShowMapper {

    List<RegularShowEntity>  getRegularShowsByFileRinseDetailId(Long fileRinseDetailId);
}
