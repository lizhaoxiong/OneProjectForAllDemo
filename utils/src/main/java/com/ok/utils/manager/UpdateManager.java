package com.ok.utils.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.ok.utils.utils.log.LogUtils;
import com.ok.utils.widget.GAlertDialog;


/**
 * Created by lizx on 2016/5/9
 *
 * 升级管理
 */
public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private GAlertDialog gAlertDialog;
    private Context mContext;
    private String apkUrl ="http://www.baidu.com"; //apk下载地址
    private String serverVersion = "0.0.0"; //从服务器获取的版本号
    private String updateDescription = "更新描述: 修改若干bug，增加新功能!!!";//更新内容描述信息
    private int update = 0; //是否需要升级 0=不用，1=可选升级，2=强升

    /** 构造函数 */
    public UpdateManager(Context context, String serverVersion, int update, String apkUrl, String info) {
        this.mContext = context;
        this.serverVersion =serverVersion;
        this.update = update;
        this.apkUrl = apkUrl;
        this.updateDescription = info;
    }

    /** 显示更新对话框 */
    public void showNoticeDialog() {

        switch(update)
        {
            case 0://无版本更新
                break;

            case 1://普通更新
                    gAlertDialog=showDialog();
                break;
            case 2://强制更新
                    gAlertDialog=showDialog();
                    gAlertDialog.setNegetiveBtn();
                break;
            default:
                LogUtils.d(TAG,"UpdateManager,showNoticeDialog()"+update);
                break;
        }
    }

    private GAlertDialog showDialog(){
        gAlertDialog = showDialog(mContext, "发现新版本 ：" + serverVersion, updateDescription,
                "现在更新","暂不处理",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                if(TextUtils.isEmpty(apkUrl)){
                                    apkUrl = "http://www.baidu.com";
                                }
                                intent.setData(Uri.parse(apkUrl));
                                mContext.startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }, null, false);
        gAlertDialog.setMessageLeft();
        return gAlertDialog;
    }

    /**
     * 弹出统一风格的对话框
     * @param context
     * @param msg
     * @param positiveStr
     * @param negativeStr
     * @param onClickListener
     * @param dismissCallBack
     * @param isCanceledOnTouchOutside
     * @return: GAlertDialog
     */
    public static GAlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
                                          CharSequence positiveStr, CharSequence negativeStr,
                                          DialogInterface.OnClickListener onClickListener,
                                          GAlertDialog.OnDialogDismissCallBack dismissCallBack,
                                          boolean isCanceledOnTouchOutside) {
        try {
            GAlertDialog.Builder builder = new GAlertDialog.Builder(context);
            if (title != null){
                builder.setTitle(title);
            }
            builder.setMessage(msg);
            builder.setPositiveButton(positiveStr);
            builder.setNegativeButton(negativeStr);
            builder.setOnClickListener(onClickListener);
            builder.setDissmissCallBack(dismissCallBack);
            builder.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            return builder.show();
        }
        catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
        return null;
    }
}
