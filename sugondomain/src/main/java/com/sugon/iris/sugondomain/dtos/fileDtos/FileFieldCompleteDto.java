package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileFieldCompleteBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileFieldCompleteDto extends FileFieldCompleteBean {

    @ApiModelProperty(value="目的模板名称")
    private String destFileTemplateName;

    @ApiModelProperty(value="源模板名称")
    private String sourceFileTemplateName;

}
