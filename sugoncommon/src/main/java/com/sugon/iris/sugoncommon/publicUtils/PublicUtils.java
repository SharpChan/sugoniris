package com.sugon.iris.sugoncommon.publicUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * dto和entity的属性定义一样的名称，用反射进行互转
 */
public class PublicUtils {
    public static <T,K> void trans(T tin,K tout) throws IllegalAccessException {
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

}
