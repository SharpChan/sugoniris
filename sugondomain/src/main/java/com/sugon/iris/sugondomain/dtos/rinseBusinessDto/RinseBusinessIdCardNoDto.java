package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessIdCardNoBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessIdCardNoDto extends RinseBusinessIdCardNoBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;
}
