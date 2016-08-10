package com.ok.utils.eventbus.subscriber.http;

import org.json.JSONObject;

public class HttpResultResolve {

    public Object resolve(String result, HttpRequestMode httpRequestMode) throws Exception {
        final IResultParser resultParser=httpRequestMode.mParser;
        JSONObject jsonObject=new JSONObject(result);
        resultParser.resolve(jsonObject);
        return resultParser;
    }
}
