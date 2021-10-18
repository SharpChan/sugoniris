package com.sugon.iris.sugondomain.beans.rinseBusiness;

import lombok.Data;

@Data
public class RinseBusinessNullBean {

    /**
     *自增序列
     */
    private Long id;

    /**
     *模板编号
     */
    private Long fileTemplateId;

    /**
     *字段id
     */
    private Long fileTemplateDetailId;

    /**
     *替换的值
     */
    private String value;
}
