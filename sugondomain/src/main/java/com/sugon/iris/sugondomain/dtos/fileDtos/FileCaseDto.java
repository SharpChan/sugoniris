package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileCaseBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Data
public class FileCaseDto extends FileCaseBean {

    @ApiModelProperty(value="表信息列表")
   private  List<FileTableDto> fileTableDtoList;

    /**
     * 已导入的数据量
     */
    @ApiModelProperty(value="该文件已导入的数据量")
    private Integer  caseImportRowCount=0;

    /**
     * 该案件数据总量
     */
    @ApiModelProperty(value="该文件数据总量")
    private Integer caseRowCount=0;

    /**
     * 申报状态0:未申报；1：已经申报；2：未通过
     */
    @ApiModelProperty(value="申报状态0:未申报；1：已经申报；2：未通过")
    private String declarationStatus;



    /**
     * 导入压缩包或文件详细信息
     */
    @ApiModelProperty(value="导入压缩包或文件详细信息")
    List<FileAttachmentDto>  fileAttachmentDtoList;

    public List<FileAttachmentDto> getFileAttachmentDtoList(){
        if(CollectionUtils.isEmpty(fileAttachmentDtoList)){
              this.fileAttachmentDtoList = new ArrayList<>();
        }
        return this.fileAttachmentDtoList;
    }

    public void  rowCount(){
        if(CollectionUtils.isEmpty(fileAttachmentDtoList)){
            return;
        }
        for(FileAttachmentDto fileAttachmentDto : fileAttachmentDtoList){
            fileAttachmentDto.setRowCount();
            caseImportRowCount += fileAttachmentDto.getAttachmentImportRowCount();
            caseRowCount +=  fileAttachmentDto.getAttachmentRowCount();
        }
    }
}
