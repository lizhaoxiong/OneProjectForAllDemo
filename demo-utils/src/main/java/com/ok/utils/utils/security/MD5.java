package com.ok.utils.utils.security;

import java.security.MessageDigest;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description MD5加密类
 *
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public class MD5 {
	public static String getMD5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	public static void main(String[] args) {
		String test = "uid=89187079&key=ux7YIWRVw0";
		String md5 = getMD5(test);
		System.out.println(md5);
	}
}
