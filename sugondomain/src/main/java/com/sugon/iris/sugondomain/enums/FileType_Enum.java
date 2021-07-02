package com.sugon.iris.sugondomain.enums;

public enum FileType_Enum {
    ZIP(".zip","zip文件"),
    RAR(".rar","rar文件"),
    XLS(".xls","xls文件"),
    XLSX(".xlsx","xlsx文件"),
    CSV(".csv","csv文件");

    private String type;
    private String message;

    private FileType_Enum(String type, String message){
        this.type = type;
        this.message = message;
    }

    public String getType(){
        return type;
    }

    public String getMessage(){
        return message;
    }


    public static FileType_Enum getEnumByType(String type){
        for(FileType_Enum c: FileType_Enum.values()){
            if(c.getType().equals(type)){
                return c;
            }
        }
        return null;
    }
}
