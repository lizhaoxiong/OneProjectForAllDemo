package com.ok.ui.http;

import com.ok.ui.MyApplication;
import com.ok.utils.net.http.HttpConfig;

import java.util.HashMap;

/**
 *
 * @description 实现Http网络请求头的参数配置
 *
 * @author Li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class HeadHttpConfig implements HttpConfig {


	public static final String OEMID = "25";
	//private static final String REFERER = "http://tiantian.qq.com";

	@Override
	public HashMap<String, String> getHeader() {
		HashMap<String, String> headers = new HashMap<String, String>();
		String MOBILE = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;// 操作系统版本
		if (MOBILE.length() > 20) { //服务器数据库表字段设置最多20个字符
			MOBILE = MOBILE.substring(0, 20);
		}
		String SV = android.os.Build.VERSION.RELEASE;// 操作系统版本
		headers.put("did", MyApplication.IMEI);
		headers.put("network", MyApplication.netType);// 入网方式
		headers.put("oemid", OEMID);// Android 15 ,IOS 16
		headers.put("version", MyApplication.version);// 客户端软件版本 如：1.0，2.0
		headers.put("sy", SV);// 系统版本 如：2.0，2.2，2.2.1，3.1，3.1.1，3.1.2
		headers.put("language", MyApplication.language);// 语言版本
		headers.put("channel", MyApplication.channel);
		headers.put("mobile", MOBILE);// 设备型号
		headers.put("dt", MyApplication.deviceType + "");
		//headers.put("userid", String.valueOf(UserManager.getUserID()));
		//headers.put("Referer", REFERER);
		long timestamp = System.currentTimeMillis();
		headers.put("cleartext", +timestamp + ""); //明文校验
		//headers.put("meck", UserManager.getMeck());//锦堂要求增加Meck
		//headers.put("ciphertext", GGCUtils.getInstance().httpEncryptToken(UserManager.getUserID(), timestamp)); //加密校验
		return headers;
	}
}
