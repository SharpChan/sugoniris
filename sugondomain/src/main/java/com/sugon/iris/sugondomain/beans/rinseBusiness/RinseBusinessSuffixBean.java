package com.sugon.iris.sugondomain.beans.rinseBusiness;
import lombok.Data;

@Data
public class RinseBusinessSuffixBean {

    /**
     *自增序列
     */
    private Long id;

    /**
     *模板编号
     */
    private Long fileTemplateId;

    /**
     *
     */
    private Long fileTemplateDetailId;

    /**
     *后缀，一个列可能有多种后缀
     */
    private String suffix;

}
