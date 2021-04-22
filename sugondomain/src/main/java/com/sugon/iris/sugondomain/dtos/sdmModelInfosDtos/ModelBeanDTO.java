package com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos;

import com.sugon.iris.sugondomain.beans.sdmModelInfosBeans.ModelBean;
import lombok.Data;

import java.util.List;

@Data
public class ModelBeanDTO extends ModelBean {
    /**
     *这个模型的所有共有和私有用户
     */
    private UsersBeanDTO usersBeanDTO;

    /**
     * 模型的sql语句，执行状态，结果表信息
     */
    private List<ModelDatasourceBeanDTO> modelDatasourceBeanDTOList;
}
