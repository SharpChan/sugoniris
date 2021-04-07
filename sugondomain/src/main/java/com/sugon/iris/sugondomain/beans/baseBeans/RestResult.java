package com.sugon.iris.sugondomain.beans.baseBeans;

import lombok.Data;

import java.util.List;

@Data
public class RestResult<T> {
    /**
     * 成功失败标志  成功：sucess，失败：failed
     */
    private String flag = "SUCESS";

    /**
     * 返回的信息
     */
    private String message;

    /**
     * 请求的回参
     */
    private T obj;

    /**
     * 报错信息
     */
    private List<Error> errorList;
}
