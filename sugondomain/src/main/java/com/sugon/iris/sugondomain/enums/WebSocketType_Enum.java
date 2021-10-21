package com.sugon.iris.sugondomain.enums;

public enum WebSocketType_Enum {
    WS_00("WS-00","连接成功"),
    WS_01("WS-01","数据导入进度")
    ;

    private String code;
    private String message;

    private WebSocketType_Enum(String code, String message){
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
