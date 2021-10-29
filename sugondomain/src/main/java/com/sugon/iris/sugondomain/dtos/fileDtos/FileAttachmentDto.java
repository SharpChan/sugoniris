package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileAttachmentBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileAttachmentDto extends FileAttachmentBean {


    /**
     * 已导入的数据量
     */
    @ApiModelProperty(value="已导入的数据量")
    Integer  attachmentImportRowCount=0;

    /**
     * 该案件数据总量
     */
    @ApiModelProperty(value="该案件数据总量")
    Integer attachmentRowCount=0;

    /**
     * 模板信息（一个文件夹对应一个模板组）
     */
    @ApiModelProperty(value="模板信息（一个文件夹对应一个模板组）")
   private FileTemplateGroupDto fileTemplateGroupDto;

    /**
     * 申报状态0:未申报；1：已经申报；2：未通过
     */
    @ApiModelProperty(value="申报状态0:未申报；1：已经申报；2：未通过")
    private String declarationStatus;

    /**
     * 导入文件列表
     */
    @ApiModelProperty(value="导入文件列表")
   private List<FileDetailDto>  fileDetailDtoList;

    @ApiModelProperty(value="导入失败信息列表")
   private List<FileDetailDto>  fileDetailDtoFailedList;

   public List<FileDetailDto> getFileDetailDtoList(){
       if(CollectionUtils.isEmpty(fileDetailDtoList)){
           this.fileDetailDtoList = new ArrayList<FileDetailDto>();
       }
       return fileDetailDtoList;
   }

    public List<FileDetailDto> getFileDetailDtoFailedList(){
        if(CollectionUtils.isEmpty(fileDetailDtoFailedList)){
            this.fileDetailDtoFailedList = new ArrayList<FileDetailDto>();
        }
        return fileDetailDtoFailedList;
    }

   public void setRowCount(){
       if(CollectionUtils.isEmpty(fileDetailDtoList)){
           return;
       }
       for(FileDetailDto fileDetailDto : fileDetailDtoList){
           attachmentImportRowCount += fileDetailDto.getImportRowCount();
           attachmentRowCount += fileDetailDto.getRowCount();
       }
   }
}
