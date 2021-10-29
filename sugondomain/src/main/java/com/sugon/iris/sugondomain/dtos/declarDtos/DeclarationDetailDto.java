package com.sugon.iris.sugondomain.dtos.declarDtos;

import com.sugon.iris.sugondomain.beans.declarBeans.DeclarationDetailBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeclarationDetailDto extends DeclarationDetailBean {


    /**
     * 申报类型 1：删除案件；2：删除文件
     */
    @ApiModelProperty(value="申报类型 1：删除案件；2：删除文件")
    private String typeName;

    /**
     * 审核状态 0：未审核；1：审核通过；2：审核不通过；
     */
    @ApiModelProperty(value="审核状态 0：未审核；1：审核通过；2：审核不通过；")
    private String  statusName;

}
