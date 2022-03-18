package com.sugon.iris.sugondomain.beans.jmBeans;

import lombok.Data;

@Data
public class JM_t_model_local_tablecolumnsBean {

    /**
     * id
     */
    private Integer id;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段注释
     */
    private String  fieldreamrk;

    /**
     * 字段类型
     */
    private String  fieldtype;

    /**
     * 字段长度
     */
    private String  fieldlength;

    /**
     * 字段索引
     */
    private String  fieldindex;

    /**
     * 数据表编号
     */
    private String  tableid;

    /**
     * 是否是主键
     */
    private String  isprimarykey;

    /**
     * 是否为空
     */
    private String  isnull;

    /**
     *
     */
    private String  extend1;
    private String  extend2;
    private String  extend3;
    private String  extend4;
    private String  extend5;

}
