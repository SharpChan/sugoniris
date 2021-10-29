package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessSuffixBean;

@Data
public class RinseBusinessSuffixDto extends RinseBusinessSuffixBean {

    @ApiModelProperty(value="字段名称")
    private String fileTemplateDetailKey;
}
