package com.ok.utils.annotation;

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

/**
 * 像这样的类，字段和方法是私有的，而且没有对外提供set/get方法，那么就得用反射
 */
public class People {
    private String name;
    @ViewInject(name = "小米",age = 11)
    private int age;
    private String eat(String eat){
        return "吃"+eat;
    }

}
