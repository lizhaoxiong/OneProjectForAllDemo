package com.ok.ui;

import java.io.Serializable;

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
public class User implements Serializable {

    public User(String name,int age){
        this.name = name;
        this.age = age;
    }
    public String name;
    public transient int age;//该字段声明不被序列化
}
