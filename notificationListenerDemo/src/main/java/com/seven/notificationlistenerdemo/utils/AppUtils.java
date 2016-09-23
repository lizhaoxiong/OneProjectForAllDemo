package com.seven.notificationlistenerdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

public class AppUtils {

    private final static X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");

    private AppUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 安装指定路径下的Apk
     * <p>根据路径名是否符合和文件是否存在判断是否安装成功
     * <p>更好的做法应该是startActivityForResult回调判断是否安装成功比较妥当
     * <p>这里做不了回调，后续自己做处理
     */
    public static boolean installApp(Context context, String filePath) {
        if (filePath != null && filePath.length() > 4
                && filePath.toLowerCase().substring(filePath.length() - 4).equals(".apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(filePath);
            if (file.exists() && file.isFile() && file.length() > 0) {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    /**
     * 卸载指定包名的App
     * <p>这里卸载成不成功只判断了packageName是否为空
     * <p>如果要根据是否卸载成功应该用startActivityForResult回调判断是否还存在比较妥当
     * <p>这里做不了回调，后续自己做处理
     */
    public boolean uninstallApp(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 封装App信息的Bean类
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packagName;
        private String versionName;
        private int versionCode;
        private boolean isSD;
        private boolean isUser;
        private boolean isWhite;//是否拦截，排序字段

        public boolean getIsWhite() {
            return isWhite;
        }

        public void setIsWhite(boolean isWhite) {
            this.isWhite = isWhite;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSD() {
            return isSD;
        }

        public void setSD(boolean SD) {
            isSD = SD;
        }

        public boolean isUser() {
            return isUser;
        }

        public void setUser(boolean user) {
            isUser = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackagName() {
            return packagName;
        }

        public void setPackagName(String packagName) {
            this.packagName = packagName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packagName  包名
         * @param versionName 版本号
         * @param versionCode 版本Code
         * @param isSD        是否安装在SD卡
         * @param isUser      是否是用户程序
         * @param isWhite     是否白名单的
         */
        public AppInfo(String name, Drawable icon, String packagName,
                       String versionName, int versionCode, boolean isSD, boolean isUser,boolean isWhite) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackagName(packagName);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSD(isSD);
            this.setUser(isUser);
            this.setIsWhite(isWhite);
        }

    /*@Override
    public String toString() {
        return getName() + "\n"
                + getIcon() + "\n"
                + getPackagName() + "\n"
                + getVersionName() + "\n"
                + getVersionCode() + "\n"
                + isSD() + "\n"
                + isUser() + "\n";
    }*/
    }

    /**
     * 获取当前App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）
     */
    public static AppInfo getAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? getBean(pm, pi) : null;
    }

    /**
     * 得到AppInfo的Bean
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        ApplicationInfo ai = pi.applicationInfo;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        //判断应用的拦截开关
        boolean isWhite = true;//默认开启
        return new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser,isWhite);
    }

    /**
     * 获取所有已安装App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）
     * <p>依赖上面的getBean方法
     */
    public static List<AppInfo> getAllAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            if (pi != null) {
                list.add(getBean(pm, pi));
            }
        }
        return list;
    }

    /**
     * 获取所有已安装用户App信息
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context){
        List<AppInfo> list = new ArrayList<>();
        //1.获取包的管理者，获取清单文件中的所有信息
        PackageManager packageManager = context.getPackageManager();
        //2.获取系统中安装应用程序的信息
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        //3.遍历集合
        for (PackageInfo packageInfo : installedPackages) {
            //4.获取数据
            //获取应用程序包名
            String packageName = packageInfo.packageName;
            //获取应用程序的版本号
            String versionName = packageInfo.versionName;
            //获取应用程序的版本号
            int versionCode = packageInfo.versionCode;
            //获取application信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取应用程序的图标
            Drawable icon = applicationInfo.loadIcon(packageManager);
            //获取应用程序的名称
            String name = applicationInfo.loadLabel(packageManager).toString();
            //因为是否安装在SD卡中和是否是用户应用程序是以标签的形式显示在系统中，所以首先先获取系统的所有标签
            boolean isUser;
            int flags = applicationInfo.flags;
            if ((flags & applicationInfo.FLAG_SYSTEM) == applicationInfo.FLAG_SYSTEM) {
                //系统应用
                isUser = false;
            }else{
                //用户应用
                isUser = true;
            }
            //判断应用安装位置
            boolean isSD;
            if ((flags & applicationInfo.FLAG_EXTERNAL_STORAGE) == applicationInfo.FLAG_EXTERNAL_STORAGE) {
                //安装在SD卡中
                isSD = true;
            }else{
                //安装在手机中
                isSD = false;
            }
            //判断应用的拦截开关
            boolean isWhite = false;//默认不是白名单

            //5.保存数据
            AppInfo appInfo = new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser,isWhite);

            /** 用户应用程序 */
            if(appInfo.isUser){
                //6.将保存bean添加集合中
                list.add(appInfo);
            }

        }
        return list;
    }
}

