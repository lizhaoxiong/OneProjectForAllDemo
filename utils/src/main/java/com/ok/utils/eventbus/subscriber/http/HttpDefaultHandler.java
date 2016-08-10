package com.ok.utils.eventbus.subscriber.http;

import com.ok.utils.eventbus.subscriber.HttpSubscriber;

import org.json.JSONException;

public class HttpDefaultHandler extends HttpHandler{
    private HttpDefaultSender mHttpDefaultSender;

    private HttpResultResolve httpResultResolve;

    public HttpDefaultHandler(){
        mHttpDefaultSender=new HttpDefaultSender();
        httpResultResolve=new HttpResultResolve();
    }

    @Override
    public void handle(final HttpRequestMode httpRequestMode) {
        mHttpDefaultSender.send(httpRequestMode);

    }

    @Override
    public void HandleResponse(final HttpResponseMode httpResponseMode) {
        HttpSubscriber.log(httpResponseMode.toString());
        final HttpResponseMode responseMode = httpResponseMode;
        final HttpRequestMode httpRequestMode = httpResponseMode.httpRequestMode;
        final IResultParser resultParser=httpRequestMode.mParser;
        try {
            switch (responseMode.httpResponse.status) {
                case HttpResponse.STATUS_ERROR_NET:
                    resultParser.setErrorObject(ErrorObject.parseError(ErrorObject.ERROR_NET_FAIL));
                    break;
                case HttpResponse.STATUS_ERROR_SERVRE:
                    resultParser.setErrorObject(ErrorObject.parseError(ErrorObject.ERROR_SERVER_ERROR));
                    break;
                case HttpResponse.STATUS_OK:
                    httpResultResolve.resolve(httpResponseMode.httpResponse.result, httpRequestMode);
                    break;
            }
        } catch(JSONException je){
            HttpSubscriber.log("数据解析错误",je);
            resultParser.setErrorObject(ErrorObject.parseError(ErrorObject.ERROR_DATA_PARSE));
        } catch (Exception e) {
            HttpSubscriber.log("数据获取异常",e);
            resultParser.setErrorObject(ErrorObject.parseError(ErrorObject.ERROR_DATA_RESOLVE));
        } finally {
            //ingnore
        }
        postEvent(resultParser);
    }

    @Override
    public void handleCancleEvent(CancleEvent cancleEvent) {
        mHttpDefaultSender.cancleEvent(cancleEvent);
    }


}
