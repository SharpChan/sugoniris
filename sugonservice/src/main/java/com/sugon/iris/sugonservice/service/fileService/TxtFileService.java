package com.sugon.iris.sugonservice.service.fileService;

import java.io.IOException;

public interface TxtFileService {

    String  creatTxtFile(String name) throws IOException;

    void writeTxtFile(String writeStr,String filenameTemp);

    boolean deleteFile(String fileName);
}
