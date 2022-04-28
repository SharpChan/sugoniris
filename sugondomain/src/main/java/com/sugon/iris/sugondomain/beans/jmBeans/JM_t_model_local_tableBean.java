package com.sugon.iris.sugondomain.beans.jmBeans;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class JM_t_model_local_tableBean {

    private Integer id;

    private String tableName;

    private String fileName="同步数据";

    private Integer importStatus = 1;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String areaType;

    private String extend1;

    private String extend2;

    private String extend3;

    private String extend4;

    private String extend5;

    private Integer userId;

}
