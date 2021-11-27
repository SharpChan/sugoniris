package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FileParsingService {

    void test(Long userId) throws IOException;

    /**
     * 解析csv文件并且写入mpp,并对文件和文件数据进行统计
     */
    List<Long> fileParsing(Long userId, Long fileAttachmentId, List<Error> errorList) throws IOException, IllegalAccessException, InterruptedException, ExecutionException;

    /**
     * 进行自定义清洗
     * @param caseId
     * @param userId
     * @param errorList
     */
    void doUserDefinedRinse(Long caseId, Long userId, List<Error> errorList);


}
