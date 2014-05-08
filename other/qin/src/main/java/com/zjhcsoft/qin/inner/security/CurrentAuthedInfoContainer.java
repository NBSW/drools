package com.zjhcsoft.qin.inner.security;

import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;

public class CurrentAuthedInfoContainer {

    /**
     * 获取当前认证对象
     *
     * @param <E>
     * @return
     */
    public static <E extends BaseAuthedInfo> E get() {
        return (E) CURRENT_AUTHED_INFO.get();
    }

    public static void remove() {
        CURRENT_AUTHED_INFO.remove();
    }

    public static <E extends BaseAuthedInfo> void set(E authedInfo) {
        CURRENT_AUTHED_INFO.set(authedInfo);
    }

    private static final ThreadLocal<BaseAuthedInfo> CURRENT_AUTHED_INFO = new ThreadLocal<>();

}
