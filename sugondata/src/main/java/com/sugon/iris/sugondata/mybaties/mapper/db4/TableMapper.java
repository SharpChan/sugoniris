package com.sugon.iris.sugondata.mybaties.mapper.db4;


import org.apache.ibatis.annotations.Select;

public interface TableMapper {

    @Select("call insertrecord2mpp(#{insertSql})")
    public void insertrecord2mpp(String insertSql);

}
