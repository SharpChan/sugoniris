package com.sugon.iris.sugondomain.dtos.declarDtos;

import com.sugon.iris.sugondomain.beans.declarBeans.DeclarationDetailBean;
import lombok.Data;

@Data
public class DeclarationDetailDto extends DeclarationDetailBean {


    /**
     * 申报类型 1：删除案件；2：删除文件
     */
    private String typeName;

}
