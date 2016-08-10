package com.ok.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 封装StringRequest
 */
public class StringRequestCompent extends StringRequest{

    Map<String,String> mParams;

    Map<String,String> mHeaders;

    NetworkResponse mNetworkResponse;

    private StringRequestCompent(int method, String url,Map<String,String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.mParams=params;
        //mHttpResponseMode=new HttpResponseMode();
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(mHeaders!=null){
            return mHeaders;
        }
        return super.getHeaders();
    }

    public void setHeaders(Map<String,String> headers){
        mHeaders=headers;
    }
    public void addHeader(String key,String value){
        if(mHeaders==null){
            mHeaders=new HashMap<>();
        }
        mHeaders.put(key,value);
    }
    public void addHeaders(Map<String,String> headers){
        if(mHeaders==null){
            mHeaders=new HashMap<>();
        }
        mHeaders.putAll(headers);
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        this.mNetworkResponse=response;
        return super.parseNetworkResponse(response);
    }
    public NetworkResponse getNetWorkResponse(){
        return this.mNetworkResponse;
    }




    public static StringRequestCompent newStringRequest(int method, String url,Map<String,String> params, Response.Listener<String> listener, Response.ErrorListener errorListener){
        switch (method){
            case Method.POST:
                return new StringRequestCompent(method,url,params,listener,errorListener);
            case Method.GET:
            default:
                if(!url.endsWith("?")){
                    url=url.concat("?");
                    if (params != null) {
                        Set<Map.Entry<String,String>> set= params.entrySet();
                        for(Map.Entry<String,String> entry:set){
                            String key=entry.getKey();
                            String value=entry.getValue();
                            if(key==null||value==null)continue;
                            url=url.concat(key).concat("=").concat(value).concat("&");
                        }
                        url=url.substring(0,url.length()-1);
                    }
                }
                return new StringRequestCompent(method,url,null,listener,errorListener);


        }
    }





    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(getMethod()==Method.POST){
            return this.mParams;
        }
        return super.getParams();
    }




}
