package com.zjhcsoft.qin.exchange.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class HttpHelper {

    public enum METHOD {
        POST, PUT, DELETE, GET
    }

    public static HttpRequestBase method(METHOD method, String url, String contentType) throws UnsupportedEncodingException {
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        if (null != contentType) {
            httpEntity.setContentType(contentType);
        }
        return getHttpRequest(method, url, httpEntity);
    }

    public static HttpRequestBase method(METHOD method, String url, String param, String contentType) throws UnsupportedEncodingException {
        StringEntity httpEntity = new StringEntity(param,"UTF-8");
        if (null != contentType) {
            httpEntity.setContentType(contentType);
        }
        return getHttpRequest(method, url, httpEntity);
    }

    public static HttpRequestBase method(METHOD method, String url, byte[] param) throws UnsupportedEncodingException {
        HttpEntity httpEntity = MultipartEntityBuilder.create().addBinaryBody("file",param).build();
        return getHttpRequest(method, url, httpEntity);
    }

    public static HttpRequestBase method(METHOD method, String url, Map<String, String> param, String contentType) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keys = param.keySet();
        String key;
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            key = it.next();
            nvps.add(new BasicNameValuePair(key, param.get(key)));
        }
        StringEntity httpEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        if (null != contentType) {
            httpEntity.setContentType(contentType);
        }
        return getHttpRequest(method, url, httpEntity);
    }

    public static CredentialsProvider credentialsProvider(String name, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(name, password));
        return credsProvider;
    }

    public static String execute(HttpRequestBase httpRequest, CredentialsProvider credsProvider) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        if (null != credsProvider) {
            context.setCredentialsProvider(credsProvider);
        }
        CloseableHttpResponse response = httpClient.execute(httpRequest, context);
        InputStream is = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                return IOUtils.toString(is, "UTF-8");
            }
            return null;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Exception...:{}", e.getMessage());
            }
            return null;
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    private static HttpRequestBase getHttpRequest(METHOD method, String url, HttpEntity httpEntity) {
        HttpRequestBase httpRequest;
        switch (method) {
            case GET:
                httpRequest = new HttpGet(url);
                break;
            case POST:
                httpRequest = new HttpPost(url);
                ((HttpPost) httpRequest).setEntity(httpEntity);
                break;
            case PUT:
                httpRequest = new HttpPut(url);
                ((HttpPut) httpRequest).setEntity(httpEntity);
                break;
            case DELETE:
                httpRequest = new HttpDelete(url);
                break;
            default:
                httpRequest = new HttpGet(url);
        }
        return httpRequest;
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

}
