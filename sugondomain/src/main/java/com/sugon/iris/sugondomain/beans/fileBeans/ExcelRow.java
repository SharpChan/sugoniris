package com.sugon.iris.sugondomain.beans.fileBeans;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelRow {

    /**
     * 字段
     */
    private List<String> fields ;

    String getField(int index){
        if(CollectionUtils.isEmpty(fields)){
               return "";
        }
        return   (String)this.fields.get(index);
    }

    public List<String> getFields(){
        if(CollectionUtils.isEmpty(fields)){
            fields = new ArrayList<String>();
            return fields;
        }
        return fields;
    }
}
