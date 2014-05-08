package com.zjhcsoft.qin.inner;

import com.zjhcsoft.qin.exchange.utils.PropertyHelper;

public class ConfigContainer {

    public static final String FLAG_TOKEN = "_token";
    public static final String METHOD_TYPE = "_method";

    public static String FLAG_PAGE_NUMBER;
    public static String FLAG_PAGE_SIZE;
    public static String JSONP_CALLBACK;
    public static String SESSION_LOGIN_NAME;
    public static boolean IS_CLOSE_AUTH;

    static {
        FLAG_PAGE_NUMBER = null != PropertyHelper.get("qin_flag_page_number") ? PropertyHelper.get("qin_flag_page_number") : "_pageNumber";
        FLAG_PAGE_SIZE = null != PropertyHelper.get("qin_flag_page_size") ? PropertyHelper.get("qin_flag_page_size") : "_pageSize";
        JSONP_CALLBACK = null != PropertyHelper.get("qin_flag_jsonp_callback") ? PropertyHelper.get("qin_flag_jsonp_callback") : "jsonpcallback";
        SESSION_LOGIN_NAME = null != PropertyHelper.get("qin_flag_session_login_name") ? PropertyHelper.get("qin_flag_session_login_name") : "qin_login_name";
        IS_CLOSE_AUTH = null != PropertyHelper.get("qin_is_close_auth") ? Boolean.valueOf(PropertyHelper.get("qin_is_close_auth")) : false;
    }
}
