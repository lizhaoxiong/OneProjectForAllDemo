package com.ok.utils.net.http;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class NetManager {
    private HttpConfig mHttpConfig;
    private static NetManager instance = new NetManager();
    public static NetManager getInstance(){return instance;}
    public HttpConfig getHttpConfig(){return  mHttpConfig;}
    public void setHttpConfig(HttpConfig httpConfig){
        this.mHttpConfig = httpConfig;
    }
}
