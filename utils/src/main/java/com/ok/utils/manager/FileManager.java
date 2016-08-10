package com.ok.utils.manager;

import android.content.Context;
import android.os.Environment;

import com.ok.utils.app.BaseApplication;

import java.io.File;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description 获取需要文件交互模块的文件目录
 *
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public final class FileManager {
    private static final String COMPANY_NAME = "guagua";
    private static final String APP_NAME = "live";
    private static final String BASE_PATH = COMPANY_NAME + File.separator + APP_NAME;
    private static final String IMAGE = "image";
    private static final String GIFT = "gift";
    private static final String LOG = "log";
    private static final String DEBUG = "debug";
    private static final String ILLEGAL = "illegal";

    public static String getAppCachePath() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        String appCacheDir = context.getFilesDir().getAbsolutePath() + File.separator + APP_NAME;
        return appCacheDir;
    }

    public static String getImagePath() {
        return getPath(IMAGE).getAbsolutePath();
    }

    public static File getGiftPath() {
        return getPath(GIFT);
    }

    public static File getLogPath() {
        return getPath(LOG);
    }

    public static File getIllegalPath() {
        return getPath(ILLEGAL);
    }

    public static File getDebugPath() {
        return getPath(DEBUG);
    }

    private static File getPath(String fileName) {
        File file = new File(getBasepath(), fileName);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    private static File getBasepath() {
        File appWorkDir = null;

        boolean isSDCardAvailable = Environment.getExternalStorageState()
            .equals(Environment.MEDIA_MOUNTED);
        if (isSDCardAvailable)
            appWorkDir = new File(Environment.getExternalStorageDirectory(), BASE_PATH);

        if (appWorkDir == null)
            appWorkDir = new File(getAppCachePath());

        if (!appWorkDir.exists())
            appWorkDir.mkdirs();

        return appWorkDir;
    }

}
