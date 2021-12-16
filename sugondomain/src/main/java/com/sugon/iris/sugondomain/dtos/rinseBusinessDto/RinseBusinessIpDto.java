package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessIpBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessIpDto extends RinseBusinessIpBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;
    
}
