package com.sugon.iris.sugondomain.enums;

public enum ErrorCode_Enum {
    SYS_DB_001("sys-db-001","查询表出错"),
    SYS_SUGON_001("sys-db-001","调用的接口出现了未知exception"),
    SUGON_00_001("sugon-00-001","模型执行失败");


    private String code;
    private String message;

    private ErrorCode_Enum(String code, String message){
        this.code = code;
        this.message = message;
    }


    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }


    public static MenuType_Enum getValueName(String code){
        for(MenuType_Enum c: MenuType_Enum.values()){
            if(c.getName().equals(code)){
                return c;
            }
        }
        return null;
    }
}
