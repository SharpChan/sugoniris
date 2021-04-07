package com.sugon.iris.sugondomain.enums;

public enum MenuType_Enum {
    ROOT_NODE("rootNode","1","根节点"),
    BRANCH("branch","2","菜单分枝"),
    LEAF_NODE("leaf_node","3","叶子节点"),
    BUTTON("button","4","菜单按钮");

    private String name;
    private String value;
    private String describe;

    private MenuType_Enum(String name, String value, String describe){
        this.name = name;
        this.value = value;
        this.describe = describe;
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }

    public String getDescribe(){
        return describe;
    }

    public static MenuType_Enum getValueName(String code){
        for(MenuType_Enum c: MenuType_Enum.values()){
            if(c.getName().equals(code)){
                return c;
            }
        }
        return null;
    }
}
