package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessPrefixBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessPrefixDto extends RinseBusinessPrefixBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;
}
