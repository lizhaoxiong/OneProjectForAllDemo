package com.ok.utils.annotation;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
public class AnnotationMainTest {
    /**
     * 需求：
     * 获取People类中age字段上自定义注解的值，然后将该值People通过反射设置给People对象的age属性，name...
     * 暴力破解People对象的私有方法，并调用该方法
     */
    @Test
    public void test1() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        People people = new People();
        /**
         * 1.获取到People类的字节码
         */
        Class clazz = People.class;

        /**
         * 2.暴力获取字节码中的age/name字段
         */
        Field declaredFieldAge = clazz.getDeclaredField("age");
        Field declaredFieldName = clazz.getDeclaredField("name");


        /**
         * 3.获取当前字段上面的注解对象
         */
        ViewInject viewInject = declaredFieldAge.getAnnotation(ViewInject.class);
        if(viewInject!=null){

            /**
             * 4.拿到自定义注解中的参数
             */
            int age = viewInject.age();
            String name = viewInject.name();

            System.out.println(name+age);

            /**
             * 5.通过反射将这两个值设置给people对象
             */
            declaredFieldAge.setAccessible(true);//允许访问
            declaredFieldAge.set(people,age);

            declaredFieldName.setAccessible(true);
            declaredFieldName.set(people,name);

            /**
             * 6.通过反射拿到私有的方法
             */
            Method declaredMethodEat = clazz.getDeclaredMethod("eat",String.class);
            declaredMethodEat.setAccessible(true);
            Object result = declaredMethodEat.invoke(people, "fish");
            System.out.println(result);

        }else{
            System.out.println("字段上面没有自定义注解");
        }
    }
}
