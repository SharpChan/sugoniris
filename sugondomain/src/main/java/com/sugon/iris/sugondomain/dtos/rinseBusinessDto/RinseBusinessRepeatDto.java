package com.sugon.iris.sugondomain.dtos.rinseBusinessDto;

import com.sugon.iris.sugondomain.beans.rinseBusiness.RinseBusinessRepeatBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class RinseBusinessRepeatDto extends RinseBusinessRepeatBean {

    @ApiModelProperty(value="字段集合")
  private List<String> fieldList ;

  public List<String> getFieldList(){
        if(CollectionUtils.isEmpty(fieldList)){
            fieldList = new ArrayList<>();
        }
        return fieldList;
  }
}
