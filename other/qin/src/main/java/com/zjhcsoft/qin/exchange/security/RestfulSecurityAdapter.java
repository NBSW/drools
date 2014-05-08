package com.zjhcsoft.qin.exchange.security;

import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * 安全处理接口
 */
public interface RestfulSecurityAdapter<E extends BaseAuthedInfo> {

    /**
     * 登录处理
     * @param authInfo
     * @return
     */
    E login(Map<String, String> authInfo);

    /**
     * 获取用户
     * @param loginName
     * @return
     */
    E getAuthInfo(String loginName);

    /**
     * 注销处理
     * @param authedInfo
     * @return
     */
    boolean logout(E authedInfo);

    /**
     * 认证处理
     * @param httpMethod
     * @param uri
     * @param authedInfo
     * @return
     */
    boolean auth(HttpMethod httpMethod, String uri, E authedInfo);

}
