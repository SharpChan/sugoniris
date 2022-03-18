package com.sugon.iris.sugondomain.beans.shengTing.base;

import lombok.Data;

import java.util.List;

@Data
public class StResponse<T> {

    private String exception;

    private String executionTime;

    private String total;

    private String periodTime;

    private T results;

    private String status;
}
