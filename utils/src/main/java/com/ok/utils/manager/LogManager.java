package com.ok.utils.manager;
/**
 *
 * @description 管理日志的类
 *  目前功能，存储日志的开关变量，要想实现功能日志的打开与关闭
 *  请调用：public static void d(Boolean isOpen,String TAG, String msg)函数
 *  将功能日志的开关定义到本类中
 *
 * @author Li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class LogManager {
    /** 调试登录的日志 */
    public static boolean LOGIN_LOG = true ;
    /** 个人主页相关的日志 */
    public static boolean PM_LOG = true;
    /** 个人信息页 */
    public static boolean PERSONAL_LOG = true ;
}
