package com.sugon.iris.sugondomain.enums;

public enum ErrorCode_Enum {
    SYS_00_000("SYS-00-000","登录已经过期"),
    SYS_01_000("SYS-01-000","用户被锁定"),
    SYS_02_000("SYS-02-000","用户未注册"),
    SYS_03_000("SYS-03-000","日志审计系统不在线"),
    SYS_DB_001("SYS-DB-001","数据表操作出错"),
    SYS_NEO4J_001("SYS-NEO4J-001","图数据库操作出错"),
    SYS_STORE_001("SYS-STORE-001","数据存入本地硬盘出错"),
    IRIS_00_001("IRIS-00-001","已经注册请直接登录！"),
    IRIS_00_002("IRIS-00-002","文件格式不满足要求"),
    IRIS_00_003("IRIS-00-003","密码错误"),
    IRIS_00_004("IRIS-00-004","存在多个账户请联系管理员"),
    IRIS_00_005("IRIS-00-005","密码必填"),
    SUGON_SSH_001("SUGON-SSH-001","获取ssh连接失败"),
    SYS_SUGON_001("SYS-SUGON-001","调用的接口出现了未知exception"),
    SUGON_00_001("SUGON-00-001","模型执行失败"),
    SUGON_01_001("SUGON-01-001","文件导入未配置数据模板"),
    SUGON_01_002("SUGON-01-002","同一模板排序编号不能重复"),
    SUGON_01_003("SUGON-01-003","数据已经同步"),
    SUGON_01_004("SUGON-01-004","请配置模板组"),
    SUGON_01_005("SUGON-01-005","参数不能为空"),
    SUGON_01_006("SUGON-01-006","还未上传数据表"),
    SUGON_01_007("SUGON-01-007","正常业务"),
    SUGON_01_008("SUGON-01-008","样式名称重复"),
    SUGON_01_009("SUGON-01-009","关键字和之前的字段重复,请配置排除字段"),
    SUGON_01_010("SUGON-01-010","字段名和之前的字段重复"),
    SUGON_01_011("SUGON-01-011","字段名：字母开头，只允许字母数字下划线"),
    SUGON_01_012("SUGON-01-012","案件编号或者案件名称重复"),
    SUGON_02_009("SUGON-02-009","与websocket通信失败"),
    SUGON_02_010("SUGON-02-010","后端接口出错"),
    FILE_01_001("FILE-01-001","文件不存在"),
    FILE_01_002("FILE-01-002","模板不存在"),
    FILE_01_003("FILE-01-003","excel文件不存在"),
    FILE_01_004("FILE-01-004","excel文件sheet名称不存在"),
    FILE_01_005("FILE-01-005","excel文件sheet名称不满足格式要求"),
    FILE_01_006("FILE-01-006","excel文件修改数据不存在"),
    FILE_01_007("FILE-01-007","模板字段不存在"),
    FILE_01_008("FILE-01-008","导入文件不匹配"),
    FILE_01_009("FILE-01-009","字段不匹配"),
    FILE_01_010("FILE-01-010","模板与文件都不匹配"),
    FILE_01_011("FILE-01-011","表头不能为空，或表头不能重复"),
    FILE_01_012("FILE-01-012","数据文件不允许匹配多个模板，请设置排除字段"),
    FILE_01_013("FILE-01-013","多个关键字匹配不允许关键字相互包含"),
    FILE_01_014("FILE-01-014","模板组内的模板不允许关键字包含"),
    FILE_01_015("FILE-01-015","csv文件内数据格式不满足要求"),
    FILE_01_016("FILE-01-016","文件解析未预知错误")
    ;

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
