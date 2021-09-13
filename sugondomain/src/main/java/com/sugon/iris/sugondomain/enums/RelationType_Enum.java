package com.sugon.iris.sugondomain.enums;

public enum RelationType_Enum {
    RE_01("01","xxx关系")
    ;

    private String code;
    private String message;

    private RelationType_Enum(String code, String message){
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
