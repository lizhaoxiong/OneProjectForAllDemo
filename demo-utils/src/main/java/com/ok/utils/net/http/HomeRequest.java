package com.ok.utils.net.http;

import com.ok.utils.bean.LiveUserInfo;
import com.ok.utils.contant.ApiConstant;

import java.util.HashMap;
import java.util.Map;

public class HomeRequest extends BaseRequest implements ApiConstant {
    private static final String TAG = "HomeRequest" ;
    public HomeRequest() {
    }

    /**
     * 请求用户信息
     * @param uid
     */
    public void reqUserInfo(long uid) {
        Map<String,String> params = new HashMap<>();
        params.put("targetUid",uid+ "");
        get(URL_USER_INFO,params,null,new LiveUserInfo());
    }
}
