package com.ok.utils.bean;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 用户信息
 */
public class LiveUserInfo extends BaseBean {
	private static final long serialVersionUID = 1L;

	public long guagua_id;
	public String meck;
	public String guagua_name;
	public String face;
	public String coin = "";//呱呱币
	public String preCoin = "-1";
	public boolean isPay = false;
	public String nobleLevel;//贵族等级
	public String diamondLevel;//钻石等级
	public String fansLevel;//粉丝等级
	public String fansName;//粉丝称谓
	public int age;//年龄
	public int gender = 0;//性别  0男 1女
	public String idiograph = "";//个人说明
	public String country;//国
	public String province;//省
	public String city;//市
	public int constellation;//星座
	public int ishowprod = 0; //是否有权限进入我的作品 （目前只有主播帐号可以进入） 1： 有权限， 0： 无权限
	public boolean isNP = false;//是否是新手
	public String headImgSmall = "";
	public String headImgMid = "";
	public String headImgBig = "";
	public int level ;
	public String place = "";
	public long follow ;
	public long follower;
	public boolean isOpenPush ;
	public int isAdmin=0;
	public String webToken="";
	public long totalMicTime;
	public long monthMicTime;
	public LiveUserInfo() {

	}

	public LiveUserInfo(JSONObject o) {
		guagua_id = getLong(o, "userid");
		isNP = getInt(o, "isnp") == 1 ? true : false ;
		meck = getString(o, "meck");
		//这里用户名和个性签名改为用原有的JSon解析，未使用解析方法
		//是由于用户名可能是"null"这个字符串
		guagua_name = o.optString("nickname");
		idiograph = o.optString("idiograph");
		age = getInt(o, "age");
		age = age < 1 ? 1 : age;
		province = getString(o, "province");
		country = getString(o, "country");
		city = getString(o, "city");
		constellation = getInt(o, "constellation");
		constellation = constellation < 0 || constellation > 11 ? 0 : constellation;
		JSONArray faceArray = getArray(o, "face");
		if (faceArray != null && faceArray.length() > 0) {
			face = getArrayString(faceArray, 0);
		}
		JSONObject level = getJSONObject(o, "level");
		if (level != null) {
			nobleLevel = getString(level, "noblelevel");
			fansLevel = getString(level, "fanlevel");
			fansName = getString(level, "fanname");
			diamondLevel = getString(level, "diamondlevel");
		}
		ishowprod = getInt(o, "ishowprod", 0);
		totalMicTime = getLong(o,"totalMicTime");
		monthMicTime = getLong(o,"monthMicTime");
	}

	@Override
	public void parse(JSONObject contentJson) throws Exception {
		super.parse(contentJson);
		JSONObject obj = null;
		/** 先从UserManger中拿,也就是说当获取到服务器数据后需要存储起来*/
//		if(contentJson.has(UserManager.getUserID() + "")) {
//			obj = contentJson.getJSONObject(UserManager.getUserID() + "");
//		}
//		else {
			Iterator iter = contentJson.keys() ;
			while (iter.hasNext()) {
				String key = (String)iter.next() ;
				obj = contentJson.getJSONObject(key) ;
//			}
//		}

		if(obj!=null) {
			headImgSmall = getString(obj,"headImgSmall");
			headImgMid = getString(obj,"headImgMid");
			headImgBig = getString(obj,"headImgBig");
			level = getInt(obj,"level");
			guagua_name = obj.optString("nickname");
			guagua_name = guagua_name.trim();
			idiograph = obj.optString("description");
			if(idiograph.equals("null")) {
				idiograph = "";
			}
			idiograph = idiograph.trim();
			gender = getInt(obj,"gender");
			place = obj.optString("place");
			follow = getInt(obj,"follow");
			follower = getInt(obj,"follower");
			totalMicTime = getLong(obj,"totalMicTime");
			monthMicTime = getLong(obj,"monthMicTime");
		}
	}
}
}
