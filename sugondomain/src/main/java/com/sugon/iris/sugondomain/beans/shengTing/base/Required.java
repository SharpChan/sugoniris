package com.sugon.iris.sugondomain.beans.shengTing.base;

import lombok.Data;

@Data
public class Required {

    private String cnt = "1";

    private String condition;

    private String maxRownum="1";

    private String minRownum="1";

    private String requiredItems;

}
