package com.sugon.iris.sugondata.mybaties.mapper.db4;


import java.util.List;

public interface MppMapper {

    Integer mppSqlExec(String sql);

    List<String> mppSqlExecForSearch(String sql);

}
