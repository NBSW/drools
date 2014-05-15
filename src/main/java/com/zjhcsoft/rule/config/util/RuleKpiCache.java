package com.zjhcsoft.rule.config.util;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;

import java.util.*;

/**
 * Created by XuanLubin on 2014/5/13. 16:09
 */
public class RuleKpiCache {
    private static Map<String, RuleKpiDefine> CACHE = new HashMap<>();

    public static RuleKpiDefine get(String kpiCode) {
        return CACHE.get(kpiCode);
    }

    public static void update(String kpiCode, RuleKpiDefine define) {
        if (null == define) {
            CACHE.remove(kpiCode);
        } else {
            CACHE.put(kpiCode, define);
        }
    }

    static {//每一小时清除
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CACHE.clear();
            }
        }, new Date(), 60 * 60 * 1000l);
    }
}
