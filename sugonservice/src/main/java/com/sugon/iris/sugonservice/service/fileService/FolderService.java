package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface FolderService {

    void test(User user);

    //进行数据同步
    void dataSync(User user,String[] selectedArr,List<Error> errorList) throws IOException;

    void decompress( User user,String[] selectedArr,List<Error> errorList) throws Exception;

    //上传文件写到文件服务器
    int uploadFile(User user, List<MultipartFile> files, Long caseId, List<Error> errorList) throws Exception;

    //删除文件
    int deleteFile(User user, String[] idArr,boolean flag, List<Error> errorList) throws Exception;

    //查询文件信息
    List<FileAttachmentDto> findFileAttachmentList(FileAttachmentDto fileAttachmentDto , List<Error> errorList) throws IllegalAccessException;

    Integer csvReadOperation(String userId,Long caseId,String filePath );

    /**
     *
     *修改模板组
     */
    Integer updateFileAttachmentTemplateGroup(User user,FileAttachmentDto fileAttachmentDto , List<Error> errorList) throws IllegalAccessException;

    String getFileServerIp();

    /**
     * 进行固定数据补全
     */
    void doFixedDefinedComplete(Long userId, Map<Long, Set<Long>> caseId2TemplateGroupIdsMap, List<Error> errorList) throws InterruptedException;

    int test();

    void doFixedCompleteByRemaining(Set<Long> caseIdSet,List<Error>  errorList);

    void doRemoveRepeat( Set<Long> caseIdSet, List<Error> errorList) throws IllegalAccessException, InterruptedException, ExecutionException;

    void regularCompleteField(Long userId,List<Long> fileIdList, List<Error> errorList) throws IllegalAccessException, IOException, SQLException;

    void doFixedDefinedCompleteInCahe(Long userId, Map<Long,Set<Long>> caseId2TemplateGroupIdsMap, List<Error> errorList) throws InterruptedException;
}
