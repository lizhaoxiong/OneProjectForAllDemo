package com.ok.utils.eventbus.subscriber.http;

import org.json.JSONObject;

public abstract class IResultParser<R> {

    public abstract void parse(R result) throws Exception;

    private ErrorObject mErrorObject;
    private HttpRequester mHttpRequester;

    public Object getTag() {
        return object;
    }

    public void setTag(Object object) {
        this.object = object;
    }

    private Object object;

    protected void resolve(R result) throws Exception {
        ResultParserWrapper resultParserWrapper = new ResultParserWrapper(this);
        resultParserWrapper.parse((JSONObject) result);
    }

    public boolean isNeedWrapper() {
        return true;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.mErrorObject = errorObject;
    }

    public ErrorObject getErrorObject() {
        return this.mErrorObject;
    }

    public int getErrorCodeID() {
        if (mErrorObject == null) {
            return 0;
        }
        return mErrorObject.getErrorID();
    }

    public boolean isSuccess() {
        return mErrorObject == null;
    }

    public HttpRequester getmHttpRequester() {
        return mHttpRequester;
    }

    public void setmHttpRequester(HttpRequester mHttpRequester) {
        this.mHttpRequester = mHttpRequester;
    }

    public int getCode() {
        return mErrorObject != null ? mErrorObject.getCode() : 0;
    }

    public String getMessage() {
        return mErrorObject != null ? mErrorObject.getMessage() : "";
    }
}
