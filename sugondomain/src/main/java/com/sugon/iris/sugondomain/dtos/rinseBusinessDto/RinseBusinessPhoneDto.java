package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessPhoneBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessPhoneDto extends RinseBusinessPhoneBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;

}
