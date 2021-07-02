package com.sugon.iris.sugonannotation.annotation.system;


import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {


}
