package com.sugon.iris.sugondomain.enums;

public enum ErrorCode_Enum {
    SYS_00_000("SYS-00-000","登录已经过期"),
    SYS_01_000("SYS-01-000","用户被锁定"),
    SYS_02_000("SYS-02-000","用户未注册"),
    SYS_DB_001("SYS-DB-001","数据表操作出错"),
    SYS_STORE_001("SYS-STORE-001","数据存入本地硬盘出错"),
    IRIS_00_002("IRIS-00-002","文件格式不满足要求"),
    IRIS_00_003("IRIS-00-003","密码错误"),
    IRIS_00_004("IRIS-00-004","存在多个账户请联系管理员"),
    SUGON_SSH_001("SUGON-SSH-001","获取ssh连接失败"),
    SYS_SUGON_001("SYS-SUGON-001","调用的接口出现了未知exception"),
    SUGON_00_001("SUGON-00-001","模型执行失败"),
    SUGON_01_001("SUGON-01-001","文件导入未配置数据模板"),
    SUGON_01_002("SUGON-01-002","同一模板排序编号不能重复"),
    SUGON_01_003("SUGON-01-003","数据已经同步"),
    SUGON_01_004("SUGON-01-004","请配置模板组");




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


    public static ErrorCode_Enum geEnumByCode(String code){
        for(ErrorCode_Enum c: ErrorCode_Enum.values()){
            if(c.getCode().equals(code)){
                return c;
            }
        }
        return null;
    }
}
