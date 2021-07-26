package com.sugon.iris.sugoncommon.publicUtils;

import com.google.common.collect.Lists;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * dto和entity的属性定义一样的名称，用反射进行互转
 */
public  class PublicUtils {

    private static final String JAVA_TYPE = "java.lang.String";
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String[]  KEYWORD = {"create","table","insert","delete","%",
                                              "update","set","where","and","or","drop",
                                              "alter","add","not","all","*","join","between","null","not"};

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
            return tout;
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
        beanAttributeValueTrim(tout);
        return tout;
    }


    /**
     * 校验sql中有没有关键字,有的话进行剔除
     */
    public static String checkSql(String str){
        for(String key : KEYWORD){
           if(str.contains(key)){
              str = str.replaceAll(key,"");
           }
        }
    return str;
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


    /**
     * 循环去除每个字段的空格
     *
     * @param bean 实体
     */
    private static void beanAttributeValueTrim(Object bean) {
        if (bean != null) {
            //获取所有的字段包括public,private,protected,private
            List<Field> fieldList = Lists.newArrayList(bean.getClass().getDeclaredFields());
            fieldList.stream().forEach(field -> {
                //判断每个字段是否是sting类型，只有是string类型时才能去除前后空格
                if (JAVA_TYPE.equals(field.getType().getName())) {
                    //获取每个字段的字段名
                    String fieldName = field.getName();
                    try {
                        //利用java反射机制获取对应字段的值
                        Object value = getFieldValue(bean, fieldName);
                        if (null != value) {
                            // 同样利用java 反射机制将修改后的值进行赋值
                            setFieldValue(bean, fieldName, value.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 利用反射通过get方法获取bean中字段fieldName的值
     *
     * @param bean      实体
     * @param fieldName 字段
     * @return object   对应值
     * @throws Exception
     */
    private static Object getFieldValue(Object bean, String fieldName) throws Exception {
        Method method;
        // 获取对应字段的get方法名称，首字母改为大写： getName
        StringBuffer methodName = new StringBuffer();
        methodName.append(GET).append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1));
        //获取对应的方法
        method = bean.getClass().getMethod(methodName.toString(), new Class[0]);
        return method.invoke(bean);
    }

    /**
     * 利用发射调用bean.set方法将value设置到字段
     *
     * @param bean       实体
     * @param fieldName  字段
     * @param fieldValue 赋值
     * @throws Exception
     */
    private static void setFieldValue(Object bean, String fieldName, String fieldValue) throws Exception {
        Class[] classArr = new Class[1];
        StringBuffer methodName = new StringBuffer();
        // 获取对应字段的set方法名称，首字母改为大写：setName
        methodName.append(SET).append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1));
        //利用发射调用bean.set方法将value设置到字段
        classArr[0] = JAVA_TYPE.getClass();
        Method method = bean.getClass().getMethod(methodName.toString(), classArr);
        method.invoke(bean, fieldValue.trim());
    }
}
