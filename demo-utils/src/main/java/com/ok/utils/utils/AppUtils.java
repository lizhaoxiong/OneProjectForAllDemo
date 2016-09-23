package com.ok.utils.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.ok.utils.utils.log.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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

    /**
     * 版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String name = appInfo.metaData.getString("version_name");
            if (name != null) {
                return name;
            }
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编译版本号
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String name = appInfo.metaData.getString("versionCode");
            if (name != null) {
                return name;
            }
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode+"";
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 渠道号
     * @param context
     * @param metaName
     * @return
     */
    public static String getChannel(Context context, String metaName) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(metaName);

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 打开指定包名的App
     */
    public static boolean openAppByPackageName(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            PackageManager pm = context.getPackageManager();
            Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packageName);
            if (launchIntentForPackage != null) {
                context.startActivity(launchIntentForPackage);
                return true;
            }
        }
        return false;
    }

    /**
     * 打开指定包名的App应用信息界面
     */
    public static boolean openAppInfo(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 可用来做App信息分享
     */
    public static void shareAppInfo(Context context, String info) {
        if (!TextUtils.isEmpty(info)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, info);
            context.startActivity(intent);
        }
    }

    /**
     * 检测服务是否运行
     * @param context   上下文
     * @param className 类名
     * @return 是否运行的状态
     */
    public static boolean isServiceRunning(Context context, String className) {
        if (context == null) return false;
        if (TextUtils.isEmpty(className)) return false;
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 停止运行服务
     * @param context   上下文
     * @param className 类名
     * @return 是否执行成功
     */
    public static boolean stopRunningService(Context context, String className) {
        if (context == null) return false;
        if (TextUtils.isEmpty(className)) return false;
        Intent intentService = null;
        boolean ret = false;
        try {
            intentService = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            Logger.e(e);
        }
        if (intentService != null) {
            // TODO 此处的context需要为Activity？
            ret = context.stopService(intentService);
        }
        return ret;
    }

    /**
     * whether this process is named with processName
     * @param context     上下文
     * @param processName 进程名
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null,
     * return false</li>
     * <li>if one process of
     * {@link ActivityManager#getRunningAppProcesses()} is equal to
     * processName, return true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null || TextUtils.isEmpty(processName)) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager
                .getRunningAppProcesses();
        if (processInfoList == null) {
            return true;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : manager
                .getRunningAppProcesses()) {
            if (processInfo.pid == pid
                    && processName.equalsIgnoreCase(processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     * @param context 上下文
     * @return if application is in background return true, otherwise return
     * false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null
                    && !topActivity.getPackageName().equals(
                    context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取应用的签名信息
     * @param context
     * @return
     */
    static public String getSingInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        Signature signature = getApkSignature(context);
        if (signature == null) return "";
        ByteArrayInputStream bais = new ByteArrayInputStream(signature.toByteArray());
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(bais);
            String version = String.valueOf(certificate.getVersion());
            String serialNumber = certificate.getSerialNumber().toString(16);
            String subjectDN = certificate.getSubjectDN().toString();
            String issuerDN = certificate.getIssuerDN().toString();
            String notBefore = certificate.getNotBefore().toString();
            String notAfter = certificate.getNotAfter().toString();
            String sigAlgName = certificate.getSigAlgName();
            byte[] sig = certificate.getSignature();
            String signatureStr = new BigInteger(sig).toString(16);
            PublicKey publicKey = certificate.getPublicKey();
            byte[] pkenc = publicKey.getEncoded();
            StringBuilder pkencSB = new StringBuilder();
            for (int i=0; i<pkenc.length; i++) {
                pkencSB.append(i+",");
            }

            sb.append(version);
            sb.append(serialNumber);
            sb.append(serialNumber);
            sb.append(subjectDN);
            sb.append(issuerDN);
            sb.append(notBefore);
            sb.append(notAfter);
            sb.append(sigAlgName);
            sb.append(signatureStr);
            sb.append(pkencSB);

            Logger.e("版本号 " + version);
            Logger.e("序列号 " + serialNumber);
            Logger.e("全名 " + subjectDN);
            Logger.e("签发者全名n" + issuerDN);
            Logger.e("有效期起始日 " + notBefore);
            Logger.e("有效期截至日 " + notAfter);
            Logger.e("签名算法 " + sigAlgName);
            Logger.e("签名n " + signatureStr);
            Logger.e("公钥 " + pkencSB.toString());
            Logger.e("SingInfo " + sb.toString());
        } catch (CertificateException e) {
            Logger.e(e);
        }

        return sb.toString();
    }

    static private Signature getApkSignature(Context context) {
        PackageInfo packageInfo = null;
        Signature signature = null;
        try {
            packageInfo = context.
                    getPackageManager().
                    getPackageInfo(
                            context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            signature = packageInfo.signatures[0];
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
        }
        return signature;
    }

    /**
     * 检测当前应用是否是Debug版本
     *
     * @param ctx
     * @return
     */
    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (int i = 0; i < signatures.length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf
                        .generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
            return false;
        } catch (CertificateException e) {
            Logger.e(e);
            return false;
        }
        return debuggable;
    }

    static public String getApkMD5(Context context) {
        String apkMD5 = "";
        Signature signature = getApkSignature(context);
        if (signature == null) return apkMD5;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
        }
        messageDigest.update(signature.toByteArray());
        byte[] digest = messageDigest.digest();
        apkMD5 = toHexString(digest);
        Logger.e("Apk MD5 : "+apkMD5);
        return apkMD5;
    }

    static public String getApkSHA1(Context context) {
        String apkSHA1 = "";
        Signature signature = getApkSignature(context);
        if (signature == null) return apkSHA1;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
        }
        messageDigest.update(signature.toByteArray());
        byte[] digest = messageDigest.digest();
        apkSHA1 = toHexString(digest);
        Logger.e("Apk SHA1 : " + apkSHA1);
        return apkSHA1;
    }

    static private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    static private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }



    /**
     * 获取CPU的核心数
     */
    public static int getAvailableProcessors(){

        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 虚拟机给每个进程分配的最大内存 m
     */
    public static float getRuntimeMaxMemory(){
        return Runtime.getRuntime().maxMemory()/(1024*1024);
    }

    /**
     * 当前进程已经占用过的内存 m
     */
    public static float getRuntimeTotalMemory(){
        return Runtime.getRuntime().totalMemory()/(1024*1024);
    }

    /**
     * 当前申请过又没用上空闲下来的可用内存 m
     */
    public static float getRuntimeFreeMemory(){
        return Runtime.getRuntime().totalMemory()/(1024*1024);
    }


}

