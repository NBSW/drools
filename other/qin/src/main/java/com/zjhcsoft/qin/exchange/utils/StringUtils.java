package com.zjhcsoft.qin.exchange.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-9
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static boolean isBlank(String param){
        if(param==null||"".equals(param)){
            return true;
        }else
            return false;
    }

    public static boolean isNotBlank(String param){
        if(param!=null&&!"".equals(param)){
            return true;
        }else
            return false;
    }

}
