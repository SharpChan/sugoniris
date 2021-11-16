package com.sugon.iris.sugondomain.beans.baseBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RestResult<T> {
    /**
     * 成功失败标志  成功：sucess，失败：failed
     */
    //@ApiModelProperty(value="成功失败标志  成功：sucess，失败：failed")
    private String flag = "SUCESS";

    /**
     * 返回的信息
     */
    //@ApiModelProperty(value="返回的信息")
    private String message;

    /**
     * 请求的回参
     */
    //@ApiModelProperty(value="泛型具体返回参数")
    private T obj;

    /**
     * 报错信息
     */
    //@ApiModelProperty(value="报错信息")
    private List<Error> errorList;
}
