package com.ok.utils.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by CYWLKJ22 on 2015/8/11.
 */
public class GoActivityUtil {
    /**
     * 开启一个正常的activity
     * @param context
     * @param toActivity
     */
    public static void goNormalActivity(Context context,Class<?> toActivity){
        context.startActivity(new Intent(context, toActivity));
    }
    public static void goFlagActivity(Context context,Class<?> toActivity,int flag){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag));
    }
    public static void goBooleanFlagActivity(Context context,Class<?> toActivity,boolean flag){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag));
    }
    public static void goStringFlagActivity(Context context,Class<?> toActivity,String flag){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag));
    }
    public static void goFloatFlagActivity(Context context,Class<?> toActivity,float flag,int day){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag).putExtra("day", day));
    }
    public static void goTwoFlagActivity(Context context,Class<?> toActivity,int flag,int type){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag).putExtra("type", type));
    }
    public static void goTwoStringFlagActivity(Context context,Class<?> toActivity,String flag,int type){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag).putExtra("type",type));
    }
    public static void goStringTwoFlagActivity(Context context,Class<?> toActivity,String flag,String type){
        context.startActivity(new Intent(context, toActivity).putExtra("flag",flag).putExtra("type",type));
    }
}
