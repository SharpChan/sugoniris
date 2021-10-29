package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileTemplateDto extends FileTemplateBean {

    /**
     *清洗字段类型组名称
     */
    @ApiModelProperty(value="清洗字段类型组名称")
    private String fileRinseName;

    /**
     * 清洗字段组
     */
    @ApiModelProperty(value="清洗字段组")
    private FileRinseGroupDto fileRinseGroupDto;

    /**
     * 模板的字段信息
     */
    @ApiModelProperty(value="模板的字段信息")
    private List<FileTemplateDetailDto> fileTemplateDetailDtoList;


    public List<FileTemplateDetailDto> getFileTemplateDetailDtoList(){
        if(CollectionUtils.isEmpty(fileTemplateDetailDtoList)){
            this.fileTemplateDetailDtoList = new ArrayList<FileTemplateDetailDto>();
        }
        return this.fileTemplateDetailDtoList;
    }
}
