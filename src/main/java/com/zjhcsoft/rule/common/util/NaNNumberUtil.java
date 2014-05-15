package com.zjhcsoft.rule.common.util;

/**
 * Created by XuanLubin on 2014/5/14. 16:09
 */
public class NaNNumberUtil {
    public static boolean isNaN(float val) {
        return Float.compare(val, Float.POSITIVE_INFINITY) == 0 || Float.compare(val, Float.NEGATIVE_INFINITY) == 0 || Float.compare(val, Float.NaN) == 0;
    }
}
