package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FolderService {

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
}
