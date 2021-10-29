package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateGroupBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FileTemplateGroupDto extends FileTemplateGroupBean {


    /**
     * 配置的模板列表（一个模板组内有多个模板）
     */
    @ApiModelProperty(value="配置的模板列表（一个模板组内有多个模板）")
    public List<FileTemplateDto> fileTemplateDtoList;

    /**
     * 用于标签显示
     */
    @ApiModelProperty(value="用于标签显示")
    public List<String> selectedCategories;



}
