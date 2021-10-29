package com.sugon.iris.sugondomain.beans.baseBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Error {

    @ApiModelProperty(value="报错编码")
    private String errorCode;

    @ApiModelProperty(value="报错信息")
    private String errorMessage;

    @ApiModelProperty(value="报错堆栈信息")
    private String exception;

    public Error(String errorCode, String errorMessage, String exception){

        this.errorCode  = errorCode;

        this.errorMessage = errorMessage;

        this.exception = exception;
    }

    public Error(String errorCode, String errorMessage){

        this.errorCode  = errorCode;

        this.errorMessage = errorMessage;
    }
}
