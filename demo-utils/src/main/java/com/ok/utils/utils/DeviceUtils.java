package com.ok.utils.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ok.utils.utils.log.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Pattern;

public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        if (null == macAddress) {
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    /**
     * 获取设备厂商，如Xiaomi
     */
    public static String getManufacturer() {
        String MANUFACTURER = Build.MANUFACTURER;
        return MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取设备SD卡是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取设备SD卡路径
     * <p>一般是/storage/emulated/0/
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    private static String[] kKNOWN_PIPES = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"};
    private static String[] kKNOWN_QEMU_DRIVERS = { "goldfish" };
    private static String[] kKNOWN_FILES = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"};
    private static String[] kKNOWN_NUMBERS = { "15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584"};
    private static String[] kKNOWN_IMSI_IDS = {"310260000000000"}; // 默认的 imsi id
    private static String[] kKNOWN_DEVICE_IDs = {"000000000000000"}; // 默认ID

    public static String getPhoneInfo() {
        StringBuilder phoneInfoSB = new StringBuilder();
        phoneInfoSB.append("Product: ").append(android.os.Build.PRODUCT);
        phoneInfoSB.append(", CPU_ABI: ").append(android.os.Build.CPU_ABI);
        phoneInfoSB.append(", TAGS: ").append(android.os.Build.TAGS);
        phoneInfoSB.append(", VERSION_CODES.BASE: ").append(android.os.Build.VERSION_CODES.BASE);
        phoneInfoSB.append(", MODEL: ").append(android.os.Build.MODEL);
        phoneInfoSB.append(", SDK: ").append(android.os.Build.VERSION.SDK_INT);
        phoneInfoSB.append(", VERSION.RELEASE: ").append(android.os.Build.VERSION.RELEASE);
        phoneInfoSB.append(", DEVICE: ").append(android.os.Build.DEVICE);
        phoneInfoSB.append(", DISPLAY: ").append(android.os.Build.DISPLAY);
        phoneInfoSB.append(", BRAND: ").append(android.os.Build.BRAND);
        phoneInfoSB.append(", BOARD: ").append(android.os.Build.BOARD);
        phoneInfoSB.append(", FINGERPRINT: ").append(android.os.Build.FINGERPRINT);
        phoneInfoSB.append(", ID: ").append(android.os.Build.ID);
        phoneInfoSB.append(", MANUFACTURER: ").append(android.os.Build.MANUFACTURER);
        phoneInfoSB.append(", USER: ").append(android.os.Build.USER);
        return phoneInfoSB.toString();
    }

    /**
     * 得到CPU核心数
     * @return CPU核心数
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 获取手机系统SDK版本
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 是否Dalvik模式
     * @return 结果
     */
    public static boolean isDalvik() {
        return "Dalvik".equals(getCurrentRuntimeValue());
    }

    /**
     * 是否ART模式
     * @return 结果
     */
    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART debug build".equals(currentRuntime);
    }

    /**
     * 获取手机当前的Runtime
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    public static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get",
                        String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String) get.invoke(
                            systemProperties, "persist.sys.dalvik.vm.lib",
                        /* Assuming default is */"Dalvik");
                    if ("libdvm.so".equals(value)) {
                        return "Dalvik";
                    } else if ("libart.so".equals(value)) {
                        return "ART";
                    } else if ("libartd.so".equals(value)) {
                        return "ART debug build";
                    }

                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }

    /**
     * 获取手机的MIME
     * 需要权限 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * @param context
     * @return
     */
    private static String mime(Context context) {
        String mimeStr = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mimeStr = telephonyManager.getDeviceId();
        return mimeStr;
    }

    /**
     * Android ID
     * 这个是不靠谱的，因为有时候它是null的，文档中明确说明，如果你恢复了出厂设置，那他就会改变的。而且如果你root了手机，你也可以改变这个ID
     * @param context
     * @return
     */
    private static String androidID(Context context) {
        String androidIDStr = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidIDStr;
    }

    /**
     * 获取手机WLAN的MAC地址
     * 当没有wifi的时候，是无法获得数据的
     * 需要权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    private static String wlanMAC(Context context) {
        String wlanMACStr = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wlanMACStr = wifiManager.getConnectionInfo().getMacAddress();
        return wlanMACStr;
    }

    /**
     * 蓝牙MAC地址
     * 需要权限 <uses-permission android:name="android.permission.BLUETOOTH "/>
     * @param context
     * @return
     */
    private static String bluetoothMAC(Context context) {
        String bluetoothMACStr = "";
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothMACStr = bluetoothAdapter.getAddress();
        return bluetoothMACStr;
    }

    /**
     * Pseudo-Unique ID
     * API >=9:通过“Build.SERIAL”这个属性来保证ID的独一无二
     * API < 9:我们可以通过读取设备的ROM版本号、厂商名、CPU型号
     * 和其他硬件信息来组合出一串15位的号码，
     * 这15位号码有可能重复，但是几率太小了，小到可以忽略
     * @param context
     * @return
     */
    private static String uniquePsuedoID(Context context) {
        String serialStr = null;
        String devIDShort = "" +
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 位
        try {
            serialStr = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(devIDShort.hashCode(), serialStr.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serialStr = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        serialStr = new UUID(devIDShort.hashCode(), serialStr.hashCode()).toString();
        return serialStr;
    }

    /**
     * 检测是否为模拟器
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        boolean isEmulator =
                checkByDeviceIDS(context) ||
                        checkByEmulatorBuild() ||
                        checkByPipes() ||
                        checkByQEmuDriverFile() ||
                        checkByEmulatorFiles() ||
                        checkByPhoneNumber(context) ||
                        checkByImsiIDS(context);
        return isEmulator;
    }

    private static boolean checkByDeviceIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            return true;
        }
        for (String knowdeviceid : kKNOWN_DEVICE_IDs) {
            if (knowdeviceid.equalsIgnoreCase(deviceId)) {
                Logger.d("Find ids: 000000000000000!");
                return true;
            }
        }
        Logger.d("Not Find ids: 000000000000000!");
        return false;
    }

    /**
     * 检测手机上的一些硬件信息
     */
    private static boolean checkByEmulatorBuild() {
        String BOARD = android.os.Build.BOARD;
        String BOOTLOADER = android.os.Build.BOOTLOADER;
        String BRAND = android.os.Build.BRAND;
        String DEVICE = android.os.Build.DEVICE;
        String HARDWARE = android.os.Build.HARDWARE;
        String MODEL = android.os.Build.MODEL;
        String PRODUCT = android.os.Build.PRODUCT;
        if (BOARD == "unknown" || BOOTLOADER == "unknown"
                || BRAND == "generic" || DEVICE == "generic"
                || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish") {
            Logger.d("Find Emulator by EmulatorBuild!");
            return true;
        }
        Logger.d("Not Find Emulator by EmulatorBuild!");
        return false;
    }

    /**
     * 检测“/dev/socket/qemud”，“/dev/qemu_pipe”这两个通道
     */
    private static boolean checkByPipes() {
        for (int i = 0; i < kKNOWN_PIPES.length; i++) {
            String pipes = kKNOWN_PIPES[i];
            File qemusocket = new File(pipes);
            if (qemusocket.exists()) {
                Logger.d("Find pipes!");
                return true;
            }
        }
        Logger.d("Not Find pipes!");
        return false;
    }

    /**
     * 检测驱动文件内容
     * 读取文件内容，然后检查已知QEmu的驱动程序的列表
     */
    private static boolean checkByQEmuDriverFile() {
        File driverfile = new File("/proc/tty/drivers");
        if (driverfile.exists() && driverfile.canRead()) {
            byte[] data = new byte[(int) driverfile.length()];
            try {
                InputStream inStream = new FileInputStream(driverfile);
                inStream.read(data);
                inStream.close();
            } catch (FileNotFoundException e) {
                Logger.e(e);
            } catch (IOException e) {
                Logger.e(e);
            }
            String driverdata = new String(data);
            for (String known_qemu_driver : kKNOWN_QEMU_DRIVERS) {
                if (driverdata.indexOf(known_qemu_driver) != -1) {
                    Logger.d("Find known_qemu_drivers!");
                    return true;
                }
            }
        }
        Logger.d("Not Find known_qemu_drivers!");
        return false;
    }

    /**
     * 检测模拟器上特有的几个文件
     */
    private static Boolean checkByEmulatorFiles() {
        for (int i = 0; i < kKNOWN_FILES.length; i++) {
            String file_name = kKNOWN_FILES[i];
            File qemu_file = new File(file_name);
            if (qemu_file.exists()) {
                Logger.d("Find Emulator Files!");
                return true;
            }
        }
        Logger.d("Not Find Emulator Files!");
        return false;
    }

    /**
     * 检测模拟器默认的电话号码
     */
    private static Boolean checkByPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phonenumber = telephonyManager.getLine1Number();
        for (String number : kKNOWN_NUMBERS) {
            if (number.equalsIgnoreCase(phonenumber)) {
                Logger.d("Find PhoneNumber!");
                return true;
            }
        }
        Logger.d("Not Find PhoneNumber!");
        return false;
    }

    /**
     * 检测imsi id是不是“310260000000000”
     */
    private static Boolean checkByImsiIDS(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsiid = telephonyManager.getSubscriberId();
        for (String knowimsi : kKNOWN_IMSI_IDS) {
            if (knowimsi.equalsIgnoreCase(imsiid)) {
                Logger.d("Find imsi ids: 310260000000000!");
                return true;
            }
        }
        Logger.d("Not Find imsi ids: 310260000000000!");
        return false;
    }

}
