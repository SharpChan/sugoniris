package com.sugon.iris.sugondomain.enums;

public enum BusinessLog_Enum {

    saveCase("/fileCase/saveCase","案件保存"),
    getCases("/fileCase/getCases","查询案件信息"),
    deleteCase("/fileCase/deleteCase","案件删除"),
    uploadFile("/file/uploadFile","文件上传"),
    deleteFile("/file/deleteFile","删除原始文件"),
    getCsv("/dataMerge/getCsv","导出csv"),
    mergeExport("/dataMerge/mergeExport","数据合并导出"),
    login("/account/login","用户登录"),
    logOut("/account/logOut","用户退出"),
    restPassword("/account/restPassword","密码修改"),
    userCheck("/account/userCheck","用户审核"),
    deleteUser("/account/deleteUser","用户删除"),
    saveConfig("/config/saveConfig","保存配置信息"),
    deleteConfig("/config/deleteConfig","配置信息删除"),
    searchAllTables("/search/searchAllTables","配置信息删除");

    private String url;
    private String name;

    private BusinessLog_Enum(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl(){
        return url;
    }

    public String getName(){
        return name;
    }


    public static BusinessLog_Enum getEnumByUrl(String url){
        for(BusinessLog_Enum c: BusinessLog_Enum.values()){
            if(c.getUrl().equals(url)){
                return c;
            }
        }
        return null;
    }
}
