package com.ok.utils.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {
    private static final int STRING_REQUEST=0;
    private static final int JSON_REQUEST=1;

    private static VolleyManager Instance;
    private RequestQueue mRequestQueue;

    public VolleyManager(Context context){

        mRequestQueue=Volley.newRequestQueue(context);
        mRequestQueue.start();
    }

    public static VolleyManager initialize(Context context){
        if(Instance==null){
            synchronized (VolleyManager.class){
                if(Instance==null)
                    Instance=new VolleyManager(context);
            }
        }

        return Instance;
    }

    public static VolleyManager getInstance(){
        if(Instance==null){
            throw new IllegalStateException("volleyManager must be init");
        }
        return Instance;
    }

    public void request(Request request){
        mRequestQueue.add(request);
    }

    public void cancleRequest(Object tag){
        mRequestQueue.cancelAll(tag);
    }

    public void cancleRequest(){
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                request.getTag();
                return true;
            }
        });
    }
}
