package com.sugon.iris.sugondomain.enums;

public enum SZ_JZ_RinseType_Enum {
    RINSE_01("01","交易明细表:交易证件号为空用账户信息的证件号补全")
    ;

    private String code;
    private String message;

    private SZ_JZ_RinseType_Enum(String code, String message){
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
