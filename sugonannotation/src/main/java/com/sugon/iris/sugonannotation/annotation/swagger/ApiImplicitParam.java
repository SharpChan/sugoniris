package com.sugon.iris.sugonannotation.annotation.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * swagger2
 *
 * @author: fjq
 * @date: 2021/2/8 11:17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplicitParam {
    //参数名
    String name() default "";

    //参数描述
    String value() default "";

    //参数默认值
    String defaultValue() default "";

    String allowableValues() default "";

    //是否必传
    boolean required() default false;

    String access() default "";

    //允许多个，当传入的参数为数组或者list时候，指定参数类型dataType()，属性设置为true；
    boolean allowMultiple() default false;

    //数据类型：这可以是类名或基础类型。
    String dataType() default "";

    //数据的class类
    Class<?> dataTypeClass() default Void.class;

    /**
     * 请求参数类型：
     * path：请求参数的获取：@PathVariable
     * query：请求参数的获取：@RequestParam
     * body：@RequestBody
     * header：请求参数的获取：@RequestHeader
     * form：表单数据
     */
    String paramType() default "";

    /**
     * a single example for non-body type parameters
     */
    String example() default "";

    //Examples for the parameter.  Applies only to BodyParameters
//    Example examples() default @Example(value = @ExampleProperty(mediaType = "", value = ""));

    //Adds the ability to override the detected type
    String type() default "";

    //Adds the ability to provide a custom format
    String format() default "";

    //Adds the ability to set a format as empty
    boolean allowEmptyValue() default false;

    //adds ability to be designated as read only.
    boolean readOnly() default false;

    //adds ability to override collectionFormat with `array` types
    String collectionFormat() default "";
}