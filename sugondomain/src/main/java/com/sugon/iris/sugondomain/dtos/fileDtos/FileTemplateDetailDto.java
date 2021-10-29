package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateDetailBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileTemplateDetailDto extends FileTemplateDetailBean {

    /**
     *清洗字段类型名称
     */
    @ApiModelProperty(value="清洗字段类型名称")
    private String fileRinseDetailTypeName;

    /**
     *
     */
    @ApiModelProperty(value="清洗字段组信息")
    private  FileRinseDetailDto fileRinseDetailDto;

    @ApiModelProperty(value="字段匹配列表")
    private List<String> fieldKeyList;

    @ApiModelProperty(value="字段排除列表")
    private List<String> excludeList;

    public List<String> getFieldKeyList(){
        if(CollectionUtils.isEmpty(fieldKeyList)){
            fieldKeyList = new ArrayList<>();
            return fieldKeyList;
        }
        return fieldKeyList;
    }


    public List<String> getExcludeList(){
        if(CollectionUtils.isEmpty(excludeList)){
            excludeList = new ArrayList<>();
            return excludeList;
        }
        return excludeList;
    }
}
