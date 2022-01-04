package com.sugon.iris.sugondomain.dtos.fileDtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Data
public class FileTemplateDetailDto4Txt  {

    /**
     *字段名称
     */
    @ApiModelProperty(value="字段名称")
    private String fieldName;

    /**
     *字段关键字&&进行分割
     */
    @ApiModelProperty(value="字段关键字&&进行分割")
    private String fieldKey;

    /**
     *正则表达式&&进行分割
     */
    @ApiModelProperty(value="正则表达式&&进行分割")
    private String regular;

    /**
     *排序字段
     */
    @ApiModelProperty(value="排序字段")
    private String sortNo;

    /**
     *备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     * 关键字排除&&进行分割
     */
    @ApiModelProperty(value="关键字排除&&进行分割")
    private String exclude;

    /**
     *清洗类型字段id
     */
    @ApiModelProperty(value="清洗类型字段id")
    private Long fileRinseDetailId;


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
