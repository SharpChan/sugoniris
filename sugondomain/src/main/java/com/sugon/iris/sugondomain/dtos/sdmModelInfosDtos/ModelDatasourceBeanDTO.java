package com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos;

import com.sugon.iris.sugondomain.beans.sdmModelInfosBeans.ModelDatasourceBean;
import lombok.Data;

@Data
public class ModelDatasourceBeanDTO extends ModelDatasourceBean {

     private ResultTableBeanDTO resultTableBeanDTO;

}
