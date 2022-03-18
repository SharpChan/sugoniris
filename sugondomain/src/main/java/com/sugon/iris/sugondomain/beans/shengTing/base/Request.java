package com.sugon.iris.sugondomain.beans.shengTing.base;

import lombok.Data;

@Data
public class Request {

    private RealInfo realInfo;

    private Required required;

    private Validate validate;
}
