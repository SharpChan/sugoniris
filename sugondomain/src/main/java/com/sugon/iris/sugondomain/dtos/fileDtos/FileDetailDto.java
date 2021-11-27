package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileDetailBean;
import lombok.Data;

@Data
public class FileDetailDto extends FileDetailBean {

    //该文件的，错误数据的数量（重复的数据已经被删除，其中包含错误数据）,直接查表file_parsing_failed用文件id关联
   private int errorItemCount;

}
