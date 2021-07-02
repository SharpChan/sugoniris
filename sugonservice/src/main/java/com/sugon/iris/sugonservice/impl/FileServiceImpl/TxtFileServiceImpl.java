package com.sugon.iris.sugonservice.impl.FileServiceImpl;

import com.sugon.iris.sugonservice.service.FileService.TxtFileService;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class TxtFileServiceImpl implements TxtFileService {

    private static String PATH_WINDOWS = "C:/temp/";
    private static String PATH_LINUX = File.separator+"root"+File.separator+"temp"+File.separator;

    public  String  creatTxtFile(String name) throws IOException {
        String path = "";
        String filenPath = "";
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
            path = PATH_WINDOWS;
        }
        else if (System.getProperties().getProperty("os.name").toLowerCase().contains("linux")){
            path = PATH_LINUX;
        }
            filenPath = path + name;
            File filePath = new File(path);
            if(!filePath.exists()){
                filePath.setReadable(true);
                filePath.setWritable(true);
                filePath.mkdirs();
            }
            File filename = new File(filenPath);
            if (!filename.exists()) {
                filename.setReadable(true);
                filename.setWritable(true);
                filename.createNewFile();
            }
        return filenPath;
    }

    public  void writeTxtFile(String writeStr,String filenameTemp) {
        File file = null;
        Writer outTxt = null;
        try {
            file = new File(filenameTemp);
            if (!file.exists()) {
                throw new RuntimeException("需要写入结构化数据的本地txt文件不存在");
            }
            outTxt = new OutputStreamWriter(new FileOutputStream(file,true), "UTF-8");
            outTxt.write(writeStr);
            outTxt.flush();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                outTxt.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public  boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

}


