package com.ok.utils.eventbus.subscriber.http;


import com.ok.utils.eventbus.EventBusManager;

/**
 * 负责 发
 */
public abstract class HttpSender {
    public abstract void send(final HttpRequestMode httpRequestMode);
    public abstract void cancleEvent(CancleEvent cancleEvent);
    void postEvent(HttpResponseMode httpResponseMode){
        EventBusManager.getInstance().post(httpResponseMode);
    }
}
