package com.ok.utils.eventbus.subscriber.http;

import java.util.Map;

/**
 * Created by yujintao on 16/2/26.
 */
public class HttpResponse {
    public static final int STATUS_OK=0;
    public static final int STATUS_ERROR_NET = 1;
    public static final int STATUS_ERROR_SERVRE = 2;
    String result;
    int status;
    int httpStatusCode;
    Throwable throwable;
    long networkMS;
    Map<String,String> headers;
    public HttpResponse(){

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public long getNetworkMS() {
        return networkMS;
    }

    public void setNetworkMS(long networkMS) {
        this.networkMS = networkMS;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


    @Override
    public String toString() {
        return "HttpResponse{" +
                "result='" + result + '\'' +
                ", status=" + status +
                ", httpStatusCode=" + httpStatusCode +
                ", throwable=" + throwable +
                '}';
    }
}
