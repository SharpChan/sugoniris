package com.sugon.iris.sugondomain.beans.baseBeans;

import lombok.Data;

@Data
public class Error {

    private String errorCode;

    private String errorMessage;

    private String exception;

    public Error(String errorCode, String errorMessage, String exception){

        this.errorCode  = errorCode;

        this.errorMessage = errorMessage;

        this.exception = exception;
    }
}
