package com.ok.utils.javabase;

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
public class User {
    public String name;
    public int age;

    public User(String name ,int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name+":"+age;
    }
}
