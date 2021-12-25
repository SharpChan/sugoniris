package com.sugon.iris.sugondomain.enums;

public enum BusinessLog_Enum {

    saveCase("/fileCase/saveCase","案件保存","10001"),
    getCases("/fileCase/getCases","查询案件信息","10002"),
    deleteCase("/fileCase/deleteCase","案件删除","10003"),
    uploadFile("/file/uploadFile","文件上传","10004"),
    deleteFile("/file/deleteFile","删除原始文件","10005"),
    getCsv("/dataMerge/getCsv","导出csv","10006"),
    mergeExport("/dataMerge/mergeExport","数据合并导出","10007"),
    login("/account/login","用户登录","10008"),
    logOut("/account/logOut","用户退出","10009"),
    restPassword("/account/restPassword","密码修改","10010"),
    userCheck("/account/userCheck","用户审核","10011"),
    deleteUser("/account/deleteUser","用户删除","10012"),
    saveConfig("/config/saveConfig","保存配置信息","10013"),
    deleteConfig("/config/deleteConfig","配置信息删除","10014"),
    searchAllTables("/search/searchAllTables","配置信息删除","10015");

    private String url;
    private String name;
    private String id;

    private BusinessLog_Enum(String url, String name,String id){
        this.url = url;
        this.name = name;
        this.id = id;
    }

    public String getUrl(){
        return url;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
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
