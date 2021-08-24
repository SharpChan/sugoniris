package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.Translate;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TranslateDto extends Translate {

   private  List<TranslateDto> childrenList;

   public  List<TranslateDto> getChildrenList(){
       if(null == childrenList){
           childrenList = new ArrayList<TranslateDto>();
       }
       return childrenList;
   }
}
