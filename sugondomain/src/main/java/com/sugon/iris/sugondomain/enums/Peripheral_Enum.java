package com.sugon.iris.sugondomain.enums;

public enum Peripheral_Enum {

    sugonfilerest_data2Mpp_uploadFile("/data2Mpp/uploadFile","解析文件上传数据接口");




    private String code;
    private String message;

    private Peripheral_Enum(String code, String message){
        this.code = code;
        this.message = message;
    }


    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }


    public static ErrorCode_Enum geEnumByCode(String code){
        for(ErrorCode_Enum c: ErrorCode_Enum.values()){
            if(c.getCode().equals(code)){
                return c;
            }
        }
        return null;
    }
}
