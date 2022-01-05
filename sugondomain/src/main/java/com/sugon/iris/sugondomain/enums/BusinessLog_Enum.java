package com.sugon.iris.sugondomain.enums;



public enum BusinessLog_Enum {

    saveCase("/fileCase/saveCase","案件保存","10001",type.ADD),
    getCases("/fileCase/getCases","查询案件信息","10002",type.SELECT),
    deleteCase("/fileCase/deleteCase","案件删除","10003",type.MODIFY),
    uploadFile("/file/uploadFile","文件上传","10004",type.ADD),
    deleteFile("/file/deleteFile","删除原始文件","10005",type.MODIFY),
    getCsv("/dataMerge/getCsv","导出csv","10006",type.SELECT),
    mergeExport("/dataMerge/mergeExport","数据合并导出","10007",type.SELECT),
    login("/account/login","用户登录","10008",type.LOGON),
    logOut("/account/logOut","用户退出","10009",type.SELECT),
    restPassword("/account/restPassword","密码修改","10010",type.MODIFY),
    userCheck("/account/userCheck","用户审核","10011",type.MODIFY),
    deleteUser("/account/deleteUser","用户删除","10012",type.MODIFY),
    saveConfig("/config/saveConfig","保存配置信息","10013",type.ADD),
    deleteConfig("/config/deleteConfig","配置信息删除","10014",type.MODIFY),
    searchAllTables("/search/searchAllTables","配置信息删除","10015",type.SELECT);

    private String url;
    private String name;
    private String id;
    private String czlx;
    private BusinessLog_Enum(String url, String name,String id,String czlx){
        this.url = url;
        this.name = name;
        this.id = id;
        this.czlx = czlx;
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

    //操作类型
    class type{

        public static final String LOGON = "0";

        public static final String SELECT = "1";

        public static final String ADD = "2";

        public static final String MODIFY = "3";

        public static final String REMOVE = "4";
    }
}
