package com.sugon.iris.sugonannotation.annotation.system;


import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER,ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BussLog {

}
