package com.sugon.iris.sugoncommon.publicUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * dto和entity的属性定义一样的名称，用反射进行互转
 */
public  class PublicUtils {

    /**
     * 把tin赋值到tout
     * 存放一般的配置
     */
    private static Map<String,String> configMap = new HashMap<>();

    public static Map<String, String> getConfigMap() {
        return configMap;
    }

    public static <T,K> K trans(T tin, K tout) throws IllegalAccessException {
        if(null == tin || tin instanceof Collection){
            tout = null;
            return null;
        }
        Field[] userFieldsOut = getAllFields(tout);
        Field[] userFieldsIn = getAllFields(tin);
        for(Field fieldOut : userFieldsOut){
            fieldOut.setAccessible(true);
        }
        for(Field fieldIn : userFieldsIn){
            fieldIn.setAccessible(true);
        }

        for(Field fieldOut : userFieldsOut){
            if(Modifier.isFinal(fieldOut.getModifiers())){
                continue;
            }
           for(Field fieldIn : userFieldsIn){
              if(fieldOut.getName().equals(fieldIn.getName())){
                  fieldOut.set(tout,fieldIn.get(tin));
              }
           }
        }
        return tout;
    }

    /**
     *获取指定路径下的所有文件
     *
     */
    public static List<File> getAllFile(File baseFile,List<File> list) {
        if(!baseFile.isDirectory()){
            list.add(baseFile);
            return list;
        }
        File[] fileList = baseFile.listFiles();
        for(File file : fileList){
            if(file.isDirectory()){
                getAllFile(file,list);
            }else{
                list.add(file);
            }
        }
        return list;
    }

    private static Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static void main(String[] args) {
        File file = new File("C:\\uplaodFile\\86c6adf8270a9563107ffeb03221914b");
        List<File> list = new ArrayList<>();
        List<File>  pathList= getAllFile(file,list);
        System.out.println(pathList);
    }
}
