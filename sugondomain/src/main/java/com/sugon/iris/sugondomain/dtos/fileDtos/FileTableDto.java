package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTableBean;
import lombok.Data;

@Data
public class FileTableDto extends FileTableBean {

    private FileDataGroupTableDto fileDataGroupTableDto;

    /**
     * 如果在FileDataGroupTable表存在记录，则已经勾选
     */
    private boolean isChecked;
}