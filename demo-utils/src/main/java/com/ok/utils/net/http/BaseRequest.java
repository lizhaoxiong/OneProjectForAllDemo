package com.ok.utils.net.http;

import com.android.volley.Request;
import com.ok.utils.eventbus.EventBusManager;
import com.ok.utils.eventbus.subscriber.http.HttpRequestMode;
import com.ok.utils.eventbus.subscriber.http.HttpRequester;
import com.ok.utils.eventbus.subscriber.http.IResultParser;
import com.ok.utils.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class BaseRequest {

    protected int get(String url, Map<String,String> params, Map<String,String> headers, IResultParser iResultParser){
        return request(url, Request.Method.GET, params, headers,iResultParser);
    }

    protected int request(String url,int method,Map<String,String> params,Map<String,String> headers,IResultParser iResultParser){
        return request(url,method,params,headers,iResultParser, HttpRequestMode.DEFAULT_RETRY_NUMBER);
    }

    protected int request(String url, int method, Map<String,String> params, Map<String,String> headers, IResultParser iResultParser, int retryNumber){
        HttpRequester httpRequester=HttpRequester.newInstance();
        httpRequester.setmUrl(url);
        httpRequester.setMethod(method);
        httpRequester.setHeaders(headers);
        httpRequester.addHeaders(NetManager.getInstance().getHttpConfig().getHeader());//--?
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("r", Utils.genRandomString());//随机r,防止重复
        SignHelper.addSign(params);//拼接参数
        httpRequester.setParams(params);
        postEvent(HttpRequestMode.build(httpRequester,iResultParser,retryNumber));
        return httpRequester.getRequestID();
    }

    private void postEvent(HttpRequestMode httpRequestMode){
        EventBusManager.getInstance().post(httpRequestMode);
    }
}
