package com.ok.utils.eventbus.subscriber.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ok.utils.http.StringRequestCompent;
import com.ok.utils.http.VolleyManager;

/**
 * 默认httpsender。简单实现
 */
public class HttpDefaultSender extends HttpSender {


    @Override
    public void send(final HttpRequestMode httpRequestMode) {
        final HttpRequester httpRequester = httpRequestMode.mHttpRequester;

        final HttpResponseMode httpResponseMode = new HttpResponseMode();
        httpResponseMode.httpRequestMode = httpRequestMode;
        final Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                httpResponseMode.httpResponse.status = HttpResponse.STATUS_OK;
                httpResponseMode.httpResponse.result = (String) response;
                postEvent(httpResponseMode);
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                httpResponseMode.httpResponse.throwable = error.fillInStackTrace();
                httpResponseMode.httpResponse.status = error.networkResponse == null ? HttpResponse.STATUS_ERROR_NET : HttpResponse.STATUS_ERROR_SERVRE;
                if(error.networkResponse!=null){
                    httpResponseMode.httpResponse.httpStatusCode = error.networkResponse.statusCode;
                    httpResponseMode.httpResponse.headers = error.networkResponse.headers;
                    httpResponseMode.httpResponse.networkMS = error.networkResponse.networkTimeMs;
                }

                postEvent(httpResponseMode);

            }
        };
        final StringRequestCompent mStringRequest = StringRequestCompent.newStringRequest(httpRequester.mMethod, httpRequester.mUrl, httpRequester.params, listener, errorListener);
        mStringRequest.addHeaders(httpRequester.getHeaders());
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(httpRequestMode.getTimeout(),httpRequestMode.getRetryNumber(),DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyManager.getInstance().request(mStringRequest);

    }

    @Override
    public void cancleEvent(CancleEvent cancleEvent) {
        if(cancleEvent.tag!=null) {
            VolleyManager.getInstance().cancleRequest(cancleEvent.tag);
        }

    }
}
