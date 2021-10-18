package com.sugon.iris.sugondomain.enums;

public enum SZ_JZ_RinseType_Enum {
    RINSE_01("01","联子子账户信息"),
    RINSE_02("02","交易明细分析"),
    RINSE_03("03","强制措施"),
    RINSE_04("04","人员联系方式"),
    RINSE_05("05","人员信息"),
    RINSE_06("06","人员住址"),
    RINSE_07("07","任务信息成功"),
    RINSE_08("08","任务信息失败"),
    RINSE_09("09","账户信息")
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
