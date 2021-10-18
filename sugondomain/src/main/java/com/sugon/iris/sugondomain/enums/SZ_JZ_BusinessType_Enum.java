package com.sugon.iris.sugondomain.enums;

public enum SZ_JZ_BusinessType_Enum {
    BUSINESS_01("01","根据字段去重"),
    BUSINESS_02("02","将null替换为空格")
    ;

    private String code;
    private String message;

    private SZ_JZ_BusinessType_Enum(String code, String message){
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
