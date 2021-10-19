package com.sugon.iris.sugondomain.beans.rinseBusiness;

import lombok.Data;

@Data
public class RinseBusinessCompleteBean {

    /**
     *自增序列
     */
    private Long id;

    /**
     *模板编号
     */
    private Long fileTemplateId;

    /**
     *需补全字段id
     */
    private Long fileTemplateDetailId;

    /**
     *关联字段  用逗号分隔
     */
    private String relationFieldKeys;


}
