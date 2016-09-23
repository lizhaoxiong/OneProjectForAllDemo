package com.ok.utils.eventbus.subscriber;

import com.ok.utils.eventbus.subscriber.http.HttpDefaultHandler;
import com.ok.utils.eventbus.subscriber.http.HttpHandler;
import com.ok.utils.eventbus.subscriber.http.HttpRequestMode;
import com.ok.utils.eventbus.subscriber.http.HttpResponseMode;
import com.ok.utils.utils.log.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HttpSubscriber {

    public static final String TAG=HttpSubscriber.class.getSimpleName();

    private HttpHandler mHttpHandler;


    public HttpSubscriber(){
        mHttpHandler=new HttpDefaultHandler();
    }



    /**
     * 接受http数据网络请求event
     * @param httpRequestMode
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventHttpRequest(HttpRequestMode httpRequestMode){
        mHttpHandler.handle(httpRequestMode);
    }

    /**
     * 响应http数据网络请求event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventHttpResponse(HttpResponseMode httpResponseMode){
        mHttpHandler.HandleResponse(httpResponseMode);
    }

    /**
     * 取消http网络请求
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventHttpCancleEvent(){

    }


    public static void log(String message){
        LogUtils.d(TAG,message);
    }
    public static void log(String message,Object... args){
        LogUtils.d(TAG, String.format(message, args));
    }
    public static void log(String message,Throwable e){
        LogUtils.d(TAG,message,e);
    }

}
