package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;

import java.io.IOException;
import java.util.List;

public interface FileParsingServiceCsv {

    /**
     * 解析csv文件并且写入mpp,并对文件和文件数据进行统计
     */
    boolean fileParsingCsv(Long userId, Long fileAttachmentId, List<Error> errorList) throws IOException, IllegalAccessException;
}
