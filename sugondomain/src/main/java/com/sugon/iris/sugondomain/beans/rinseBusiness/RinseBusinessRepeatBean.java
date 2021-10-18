package com.sugon.iris.sugondomain.beans.rinseBusiness;


import lombok.Data;

@Data
public class RinseBusinessRepeatBean {

    /**
     *自增序列
     */
    private Long id;

    /**
     *模板编号
     */
    private Long fileTemplateId;

    /**
     *字段id组合用逗号隔开
     */
    private String fields;

}
