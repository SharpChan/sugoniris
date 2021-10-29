package com.sugon.iris.sugondomain.dtos.declarDtos;

import com.sugon.iris.sugondomain.beans.declarBeans.DeclarInfoBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeclarInfoDto extends DeclarInfoBean {

    /**
     * 待审核，审核通过，审核未通过
     */
    @ApiModelProperty(value="待审核，审核通过，审核未通过")
    private String declarName;
}
