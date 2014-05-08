package com.zjhcsoft.rule.common.util;

import java.math.BigDecimal;

/**
 * Created by XuanLubin on 2014/4/10. 0:54
 */
public class DimFieldProcessor {
    public static String exec(Object dimValue) {
        if (null != dimValue) {
            if (dimValue instanceof Number) {
                if (dimValue instanceof Integer) {
                    return String.valueOf(dimValue);
                }
                BigDecimal val = new BigDecimal(String.valueOf(dimValue));
                return String.valueOf(val.intValue());
            }
            return String.valueOf(dimValue);
        }
        return "";
    }
}
