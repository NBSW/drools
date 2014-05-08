package com.zjhcsoft.jaxrs;

/**
 * Created by XuanLubin on 2014/4/16. 19:31
 */
public abstract class AuthService<T> {
    protected Class<T> serviceClass;
    protected String serviceUrl;

    abstract public T create();

    public void setServiceClass(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
