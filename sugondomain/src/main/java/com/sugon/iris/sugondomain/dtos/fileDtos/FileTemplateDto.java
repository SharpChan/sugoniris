package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public  boolean equals(Object o) {
        if(null == this.getId()){
            throw new RuntimeException("模板id不能为空");
        }
        if(! (o instanceof FileTemplateBean)){
             return false;
        }else{
            FileTemplateBean fileTemplateBean = (FileTemplateBean)o;
            if(this.getId().equals(fileTemplateBean.getId())){
                return true;
            }else{
                return false;
            }
        }
    }
}
