package com.sugon.iris.sugondata.mybaties.mapper.db4;


import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MppMapper {

    Integer mppSqlExec(String sql);

    Integer mppSqlExecForSearchCount(String sql);

    List<String> mppSqlExecForSearch(String sql);

    List<Long>  mppSqlExecForSearchResInteger(String sql);

    List<Map<String,Object>> mppSqlExecForSearchRtMapList(String sql);

    @Select("select nextval(#{seq})")
    public Long selectSeq(String seq);
}
