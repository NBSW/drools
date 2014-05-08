package com.zjhcsoft.rule.common.util;

/**
 * Created by XuanLubin on 2014/4/3. 14:07
 */
public class ClassPathUtil {
    public static String getClassPath(){
        String classPath = ClassPathUtil.class.getResource("").getPath();
        classPath = classPath.substring(0,classPath.indexOf("classes")+8);
        return classPath;
    }
}
