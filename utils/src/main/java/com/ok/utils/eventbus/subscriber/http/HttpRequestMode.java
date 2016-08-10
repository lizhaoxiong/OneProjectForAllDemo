package com.ok.utils.eventbus.subscriber.http;

public class HttpRequestMode {

    public static final int MAX_RETRY_NUMBER=10;
    public static final int MAX_TIME=10000;
    public static final int DEFAULT_RETRY_NUMBER=3;
    public static final int DEFAULT_TIMEOUT=1500;

    HttpRequester mHttpRequester;
    IResultParser mParser;

    private int retryNumber;
    private int timeout;


    private HttpRequestMode(HttpRequester httpRequester, IResultParser iResultParser) {
        this(httpRequester,iResultParser,DEFAULT_RETRY_NUMBER,DEFAULT_TIMEOUT);
    }

    private HttpRequestMode(HttpRequester httpRequester, IResultParser iResultParser, int retryNumber) {
        this(httpRequester,iResultParser,retryNumber,DEFAULT_TIMEOUT);
    }
    private HttpRequestMode(HttpRequester httpRequester, IResultParser iResultParser, int retryNumber, int timeout) {
        this.mHttpRequester = httpRequester;
        this.mParser = iResultParser;
        this.mParser.setmHttpRequester(httpRequester);
        this.retryNumber = retryNumber;
        this.timeout=timeout;
    }

    public static HttpRequestMode build(HttpRequester httpRequester, IResultParser parser) {
        return new HttpRequestMode(httpRequester, parser);
    }
    public static HttpRequestMode build(HttpRequester httpRequester, IResultParser parser,int retryNumber) {
        return new HttpRequestMode(httpRequester, parser,retryNumber);
    }

    public int getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(int retryNumber) {
        this.retryNumber = retryNumber;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
