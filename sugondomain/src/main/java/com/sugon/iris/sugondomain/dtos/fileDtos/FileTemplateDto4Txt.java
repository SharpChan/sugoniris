package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FileTemplateDto4Txt {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private transient Long id;
    /**
     *模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     *对应的表名关键字
     */
    @ApiModelProperty(value="对应的表名关键字")
    private String tablePrefix;

    /**
     *自动匹配的关键字以&&分隔
     */
    @ApiModelProperty(value="自动匹配的关键字以&&分隔")
    private String templateKey;

    /**
     *备注
     */
    @ApiModelProperty(value="备注")
    private String comment;


    /**
     * 关键字排除以&&分隔
     */
    @ApiModelProperty(value="关键字排除以&&分隔")
    private String exclude;

    /**
     * 清洗字段组id
     */
    @ApiModelProperty(value="清洗字段组id")
    private Long fileRinseGroupId;

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
    private List<FileTemplateDetailDto4Txt> fileTemplateDetailDtoList;


    public List<FileTemplateDetailDto4Txt> getFileTemplateDetailDtoList(){
        if(CollectionUtils.isEmpty(fileTemplateDetailDtoList)){
            this.fileTemplateDetailDtoList = new ArrayList<FileTemplateDetailDto4Txt>();
        }
        return this.fileTemplateDetailDtoList;
    }

}
