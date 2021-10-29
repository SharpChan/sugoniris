package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessNullBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessNullDto extends RinseBusinessNullBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;

}
