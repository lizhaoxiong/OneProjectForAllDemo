package com.ok.ui.inject;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
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
public class MyViewUtils {

    public static void inject(Activity activity) {
        bindView(activity);
        bindOnclick(activity);
    }

    private static void bindOnclick(final Activity activity) {
        Class clazz = activity.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method method:declaredMethods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick!=null){
                int resId = onClick.value();
                final View view = activity.findViewById(resId);
                method.setAccessible(true);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(activity,view);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }

    private static void bindView(Activity activity) {
        /**
         * 1.获取Activity的字节码
         */
        Class clazz = activity.getClass();
        /**
         * 2.获取activity的所有字段
         */
        Field[] fields = clazz.getDeclaredFields();
        /**
         * 3.遍历所有字段，判断有注解的字段
         */
        for (Field field:fields) {
            //获取字段上面的注解
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if(viewInject!=null){
                /**
                 * 4.获取字段上面的值R.id...
                 */
                int resId = viewInject.value();
                /**
                 * 5.得到控件
                 */
                View view = activity.findViewById(resId);
                /**
                 * 6.将当前的view设置给当前Filed
                 */
                field.setAccessible(true);
                try {
                    field.set(activity,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }else{
                // TODO: todoNothing 
            }
        }
    }
}
