package com.ok.utils.eventbus.subscriber.http;

import com.android.volley.Request;

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
public class HttpRequester {

    private static volatile int syncID = 1000;//--?
    public static final int RESULT_JSON_TYPE=1;//--?
    public static final int RESULT_STRING_TYPE=2;

    int mMethod;
    int requestID;
    String mUrl;
    Map<String, String> params;
    Map<String, String> mHeaders;
    int resultType;

    public Object tag;//请求时不需要的信息，但是回调会用到的信息。//--?

    public HttpRequester() {
        mMethod = Request.Method.GET;
        init();
    }

    public static HttpRequester newInstance() {
        return new HttpRequester();
    }


    private void init() {
        requestID = createID();
        resultType=RESULT_JSON_TYPE;
    }

    private static synchronized int createID() {
        if (syncID == Integer.MAX_VALUE) {
            syncID = 0;
        }
        return syncID++;
    }

    public int getRequestID() {
        return requestID;
    }


    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setMethod(int method) {
        mMethod = method;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }
    public Map<String, String> getHeaders() {return mHeaders;}
    public void addHeaders(Map<String, String> headers) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.putAll(headers);
    }

    @Override
    public String toString() {
        return "HttpRequester{" +
                "params=" + params +
                ", mMethod=" + mMethod +
                ", requestID=" + requestID +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }

}
