package com.ok.utils.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>One hundred thousand</p>
 *
 * @description 工具类
 *
 * @author li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class Utils {
	/**
	 * 创建目录
	 * @param path
	 */
	public static void createDirs(File path) {
		if (path != null && !path.exists()) {
			path.mkdirs();
		}
	}

	/**
	 * 文件是否存在
	 * @param file
	 * @return
	 */
	public static boolean isFileExist(File file) {
		if (file != null && file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * decode js用escape编码的字符串
	 * @method: unEscape
	 * @description:
	 * @author: DongFuhai
	 * @param src
	 * @return
	 * @return: String
	 * @date: 2013-10-14 下午5:57:56
	 */
	public static String unEscape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				}
				else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			}
			else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				}
				else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
    
	/**
	 * 发送自定义消息
	 */
    public static void sendEmptyMsg(Handler handler, int what) {
    	if(handler == null)
    		return;
    	
        handler.sendEmptyMessage(what);
    }
    
    /**
	 * 发送自定义消息
	 */
    public static void sendEmptyMsgDelayed(Handler handler, int what, long delayMillis) {
    	if(handler == null)
    		return;
    	
        handler.sendEmptyMessageDelayed(what, delayMillis);
    }
    
	/**
	 * 发送自定义消息
	 */
    public static void sendMsg(Handler handler, int what) {
    	if(handler == null)
    		return;
    	
        Message message = new Message();
        message.what = what;
        handler.sendMessage(message);
    }
	
	/**
	 * 发送自定义消息
	 */
    public static void sendMsg(Handler handler, int what, Object obj) {
    	if(handler == null)
    		return;
    	
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }
    
    /**
	 * 发送自定义消息
	 */
    public static void sendMsgDelayed(Handler handler, int what, Object obj, long delayMillis) {
    	if(handler == null)
    		return;
    	
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessageDelayed(message, delayMillis);
    }
	
	/**
	 * 发送自定义消息
	 */
    public static void sendMsg(Handler handler, int what, int arg1, Object obj) {
    	if(handler == null)
    		return;
    	
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.obj = obj;
        handler.sendMessage(message);
    }
    
	/**
	 * 发送自定义消息
	 */
    public static void sendMsg(Handler handler, int what, int arg1, int arg2, Object obj) {
    	if(handler == null)
    		return;
    	
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        handler.sendMessage(message);
    }

	/**
	 * 字符串转字节数组
	 * @param hexString
	 * @return
     */
	public static byte[] hexStringToBytes(String hexString) {
		if (TextUtils.isEmpty(hexString)) {
			return new byte[0];
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将源list中start到end的数据copy到destlist中,从destStart位置开始
	 * @param dest
	 * @param src
	 * @param start
	 * @param length
	 * @param <T>
	 */
	public static <T> void rangeSet(final List<T> dest, int destStart, final List<T> src, int start, int length) {
		if(destStart>dest.size()){
			throw new IllegalArgumentException("dest Start bigger dest list length ");
		}
		for (length+=start; start <length; start++,destStart++) {
			if(destStart>=dest.size()){
				dest.add(src.get(start));
			}else{
				dest.set(destStart, src.get(start));
			}
		}
	}

	/**
	 * 从指定位置开始移除集合中元素
	 * @param list
	 * @param start
	 * @param <T>
	 */
	public static <T> void rangeRemoveToEnd(final List<T> list, int start) {
		Iterator iterator = list.iterator();
		final int size = list.size();
		int i = 0;
		while (iterator.hasNext()) {
			if (i++ >= start) {
				iterator.remove();
			}
		}
	}


	/**
	 * 数据ip转ip
	 * @param ip
	 * @return
	 */
	public static String convertInt2Ip(int ip) {
		long raw = ip;
		byte[] b = new byte[] { (byte) raw, (byte) (raw >> 8), (byte) (raw >> 16), (byte) (raw >> 24) };
		try {
			return InetAddress.getByAddress(b).getHostAddress();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static int numberNotNull(Integer i){
		return i==null?0:i.intValue();
	}
	public static long numberNotNull(Long l){
		return l==null?0:l.longValue();
	}
	public static short numberNotNull(Short s){
		return s==null?0:s.shortValue();
	}

	/**
	 * 随机生成字符串
	 * @return
     */
	public static String genRandomString() {
		Random random = new Random(System.currentTimeMillis());
		int rand;

		int len = random.nextInt(10);
		if (len < 4) {
			len = 4;
		}
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < len; i++){
			rand = random.nextInt(26);
			buf.append((char)(rand + 'a'));
		}
		return buf.toString();
	}

	/**
	 * 经纬度测距
	 * @param jingdu1
	 * @param weidu1
	 * @param jingdu2
	 * @param weidu2
	 * @return
	 */
	public static double distance(double jingdu1, double weidu1, double jingdu2,   double weidu2) {
		double a, b, R;
		R = 6378137; // 地球半径
		weidu1 = weidu1 * Math.PI / 180.0;
		weidu2 = weidu2 * Math.PI / 180.0;
		a = weidu1 - weidu2;
		b = (jingdu1 - jingdu2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
				* Math.cos(weidu2) * sb2 * sb2));
		return d;
	}

}

