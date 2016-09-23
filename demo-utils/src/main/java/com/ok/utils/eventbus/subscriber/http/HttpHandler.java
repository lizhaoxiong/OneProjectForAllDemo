package com.ok.utils.eventbus.subscriber.http;


import com.ok.utils.eventbus.EventBusManager;

/**
 * 衔接发和收的中间者抽象基类
 * handle 请求 响应 请求取消 发送
 */
public abstract class HttpHandler {
    public abstract void handle(HttpRequestMode httpRequestMode);
    public abstract void HandleResponse(HttpResponseMode httpResponseMode);
    public abstract void handleCancleEvent(CancleEvent cancleEvent);
    protected void postEvent(Object o){
        EventBusManager.getInstance().post(o);
    }
}
