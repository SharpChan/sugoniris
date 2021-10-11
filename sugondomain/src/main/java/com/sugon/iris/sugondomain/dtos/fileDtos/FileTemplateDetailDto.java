package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateDetailBean;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileTemplateDetailDto extends FileTemplateDetailBean {

    /**
     *清洗字段类型名称
     */
    private String fileRinseDetailTypeName;

    /**
     *
     */
    private  FileRinseDetailDto fileRinseDetailDto;


    private List<String> fieldKeyList;

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
