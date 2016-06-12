package com.itheima.animatortest;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @author lizhaoxiong
 * @version 1.3.0
 * @description 操作assets资源文件工具类
 * @modify
 */
public class GetAssetsUtils {
    /**
     * 获取assert的图片
     * @param name
     * @param mContext
     * @return Bitmap
     */
    public static Bitmap getBitmapImage(String name, Context mContext) {
        // 1.获取assets管理者
        AssetManager assetManager = mContext.getAssets();
        InputStream in = null;
        Bitmap bmp;
        try {
            // 2.将数据转化成流信息
            in = assetManager.open(name);
            // getCacheDir() : 获取缓存目录
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (Exception e) {

                }
            }
        }
        return bmp;
    }
}
