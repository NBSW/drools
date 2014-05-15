package com.zjhcsoft.rule.datadispose.service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by XuanLubin on 2014/5/15. 17:23
 */
public class KpiRunLock {
    private static Set<String> RUN_LOCK = new HashSet<>();

    public static boolean getLock(String kpiCode, String dateCd) {
        return RUN_LOCK.add(kpiCode + "-" + dateCd);
    }

    public static void releaseLock(String kpiCode, String dateCd) {
        RUN_LOCK.remove(kpiCode + "-" + dateCd);
    }
}
