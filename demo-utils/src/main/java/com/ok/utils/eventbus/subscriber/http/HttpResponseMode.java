package com.ok.utils.eventbus.subscriber.http;

/**
 * Created by yujintao on 16/2/26.
 */
public class HttpResponseMode {
    HttpRequestMode httpRequestMode;
    HttpResponse httpResponse;
    public HttpResponseMode(){
        httpResponse=new HttpResponse();
    }

    @Override
    public String toString() {
        return "HttpResponseMode{" +
                "httpResponse=" + httpResponse +
                ",httprequest="+httpRequestMode.mHttpRequester+'}';
    }
}
