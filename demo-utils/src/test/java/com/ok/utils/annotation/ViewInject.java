package com.ok.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */

@Target(ElementType.FIELD)//注解范围
//@Retention(RetentionPolicy.SOURCE)//源码中生效
//@Retention(RetentionPolicy.CLASS)//在源码和字节码中生效,但是当虚拟机加载该这个字节码，注解信息失效
@Retention(RetentionPolicy.RUNTIME)//源码，字节码，运行时生效，全过程生效
public @interface ViewInject {
    String name();
    int age();
}
